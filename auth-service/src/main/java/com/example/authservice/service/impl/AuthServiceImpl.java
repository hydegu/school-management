package com.example.authservice.service.impl;

import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.LoginRequest;
import com.example.authservice.entity.AppUser;
import com.example.authservice.entity.Role;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.UserService;
import com.example.config.JwtProperties;
import com.example.service.TokenService;
import com.example.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    @Value("${jwt.refresh-token.use-cookie:false}")
    private boolean useHttpOnlyCookie;

    @Value("${jwt.refresh-token.cookie-secure:false}")
    private boolean cookieSecure;

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        log.info("用户登录请求: {}", request.getIdentifier());

        // 1. 查找用户
        AppUser user = userService.findByIdentifier(request.getIdentifier())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        // 2. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("用户 {} 密码错误", request.getIdentifier());
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() != 1) {
            throw new IllegalArgumentException("用户已被禁用");
        }

        // 4. 获取用户角色
        Role role = userService.selectRolesByUserId(user.getId());
        String roleName = role != null ? role.getRoleName() : "USER";

        // 5. 生成双令牌
        Map<String, Object> tokens = tokenService.generateTokens(
                user.getId().longValue(),
                user.getUsername(),
                roleName
        );

        // 6. 构建响应
        AuthResponse authResponse = buildAuthResponse(tokens, response);

        log.info("用户 {} 登录成功，RefreshToken模式: {}", user.getUsername(), authResponse.getRefreshTokenMode());
        return authResponse;
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.info("令牌刷新请求");

        try {
            // 1. 获取RefreshToken
            String token = getRefreshToken(refreshToken, httpRequest);

            // 2. 刷新令牌（使用roleProvider获取最新角色）
            Map<String, Object> newTokens = tokenService.refreshAccessToken(token, userId -> {
                Role role = userService.selectRolesByUserId(userId.intValue());
                return role != null ? role.getRoleName() : "USER";
            });

            // 3. 构建响应
            AuthResponse authResponse = buildAuthResponse(newTokens, httpResponse);

            log.info("令牌刷新成功，RefreshToken模式: {}", authResponse.getRefreshTokenMode());
            return authResponse;

        } catch (Exception e) {
            log.error("令牌刷新失败: {}", e.getMessage());
            throw new IllegalArgumentException("刷新令牌无效或已过期: " + e.getMessage());
        }
    }

    @Override
    public void logout(Long userId, HttpServletResponse httpResponse) {
        log.info("用户登出请求: userId={}", userId);

        // 1. 撤销Redis中的RefreshToken
        tokenService.revokeRefreshToken(userId);

        // 2. 如果使用Cookie，删除Cookie
        if (useHttpOnlyCookie) {
            CookieUtils.deleteCookie(httpResponse, jwtProperties.getRefreshToken().getTokenName());
            log.debug("已删除RefreshToken Cookie");
        }

        log.info("用户 {} 登出成功", userId);
    }

    /**
     * 获取RefreshToken（从Cookie或请求参数）
     */
    private String getRefreshToken(String refreshToken, HttpServletRequest httpRequest) {
        if (useHttpOnlyCookie) {
            // 从Cookie获取
            return CookieUtils.getCookieValue(httpRequest, jwtProperties.getRefreshToken().getTokenName())
                    .orElseThrow(() -> new IllegalArgumentException("未找到RefreshToken Cookie"));
        } else {
            // 从请求参数获取
            if (refreshToken == null || refreshToken.isEmpty()) {
                throw new IllegalArgumentException("请求体中缺少RefreshToken");
            }
            return refreshToken;
        }
    }

    /**
     * 构建认证响应
     */
    private AuthResponse buildAuthResponse(Map<String, Object> tokens, HttpServletResponse response) {
        AuthResponse.AuthResponseBuilder builder = AuthResponse.builder()
                .accessToken((String) tokens.get("accessToken"))
                .accessTokenExpire((Long) tokens.get("accessTokenExpire"));

        if (useHttpOnlyCookie) {
            // Cookie模式：设置HttpOnly Cookie
            String refreshToken = (String) tokens.get("refreshToken");
            int maxAge = (int) (jwtProperties.getRefreshToken().getTtl() / 1000);

            Cookie cookie = CookieUtils.createCookie(
                    jwtProperties.getRefreshToken().getTokenName(),
                    refreshToken,
                    maxAge,
                    true,           // HttpOnly
                    cookieSecure,   // Secure
                    "/",
                    null
            );
            CookieUtils.addCookie(response, cookie);

            builder.refreshTokenMode("cookie");
        } else {
            // JSON模式：在响应体中返回RefreshToken
            builder.refreshToken((String) tokens.get("refreshToken"))
                    .refreshTokenExpire((Long) tokens.get("refreshTokenExpire"))
                    .refreshTokenMode("json");
        }

        return builder.build();
    }
}
