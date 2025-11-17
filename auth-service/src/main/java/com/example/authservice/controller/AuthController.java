package com.example.authservice.controller;

import com.example.authservice.entity.AppUser;
import com.example.authservice.entity.Role;
import com.example.authservice.service.UserService;
import com.example.config.JwtProperties;
import com.example.service.TokenService;
import com.example.utils.CookieUtils;
import com.example.utils.R;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器 - 支持双令牌机制（支持HttpOnly Cookie模式）
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    /**
     * 是否使用HttpOnly Cookie存储RefreshToken（推荐开启以提高安全性）
     * 可通过配置文件设置：jwt.refresh-token.use-cookie=true
     */
    @Value("${jwt.refresh-token.use-cookie:false}")
    private boolean useHttpOnlyCookie;

    /**
     * Cookie的Secure属性（HTTPS）
     */
    @Value("${jwt.refresh-token.cookie-secure:false}")
    private boolean cookieSecure;

    /**
     * 用户登录 - 返回AccessToken和RefreshToken
     *
     * @param loginRequest 登录请求
     * @param response     HttpServletResponse（用于设置Cookie）
     * @return 双令牌
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.info("用户登录请求: {}", loginRequest.getIdentifier());

        // 查找用户
        AppUser user = userService.findByIdentifier(loginRequest.getIdentifier())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("用户 {} 密码错误", loginRequest.getIdentifier());
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new IllegalArgumentException("用户已被禁用");
        }

        // 获取用户角色
        Role role = userService.selectRolesByUserId(user.getId());
        String roleName = role != null ? role.getRoleName() : "USER";

        // 生成双令牌
        Map<String, Object> tokens = tokenService.generateTokens(
                user.getId().longValue(),
                user.getUserName(),
                roleName
        );

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", tokens.get("accessToken"));
        result.put("accessTokenExpire", tokens.get("accessTokenExpire"));

        // 根据配置决定RefreshToken的返回方式
        if (useHttpOnlyCookie) {
            // ✅ 推荐：使用HttpOnly Cookie存储RefreshToken（更安全）
            String refreshToken = (String) tokens.get("refreshToken");
            int maxAge = (int) (jwtProperties.getRefreshToken().getTtl() / 1000);

            Cookie cookie = CookieUtils.createCookie(
                    jwtProperties.getRefreshToken().getTokenName(),
                    refreshToken,
                    maxAge,
                    true,           // HttpOnly - 防止XSS攻击
                    cookieSecure,   // Secure - 仅HTTPS（生产环境建议true）
                    "/",
                    null
            );
            CookieUtils.addCookie(response, cookie);

            result.put("refreshTokenMode", "cookie");
            log.info("用户 {} 登录成功（RefreshToken已设置为HttpOnly Cookie）", user.getUserName());
        } else {
            // ⚠️ 传统方式：在响应体中返回RefreshToken（不够安全）
            result.put("refreshToken", tokens.get("refreshToken"));
            result.put("refreshTokenExpire", tokens.get("refreshTokenExpire"));
            result.put("refreshTokenMode", "json");
            log.info("用户 {} 登录成功（RefreshToken在响应体中）", user.getUserName());
        }

        return R.ok(result);
    }

    /**
     * 刷新令牌 - 使用RefreshToken获取新的双令牌
     * 支持从Cookie或请求体获取RefreshToken
     *
     * @param request        HttpServletRequest（用于读取Cookie）
     * @param response       HttpServletResponse（用于设置Cookie）
     * @param refreshRequest 刷新请求（可选，当不使用Cookie时）
     * @return 新的双令牌
     */
    @PostMapping("/refresh")
    public R<Map<String, Object>> refresh(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestBody(required = false) RefreshRequest refreshRequest) {
        log.info("令牌刷新请求");

        try {
            String refreshToken = null;

            // 优先从Cookie获取RefreshToken
            if (useHttpOnlyCookie) {
                refreshToken = CookieUtils.getCookieValue(request, jwtProperties.getRefreshToken().getTokenName())
                        .orElseThrow(() -> new IllegalArgumentException("未找到RefreshToken Cookie"));
                log.debug("从Cookie获取RefreshToken");
            } else {
                // 从请求体获取RefreshToken
                if (refreshRequest == null || refreshRequest.getRefreshToken() == null) {
                    throw new IllegalArgumentException("请求体中缺少RefreshToken");
                }
                refreshToken = refreshRequest.getRefreshToken();
                log.debug("从请求体获取RefreshToken");
            }

            // ✅ 修复：提供roleProvider从数据库获取最新角色
            Map<String, Object> newTokens = tokenService.refreshAccessToken(
                    refreshToken,
                    userId -> {
                        // 从数据库查询用户最新角色
                        Role role = userService.selectRolesByUserId(userId.intValue());
                        return role != null ? role.getRoleName() : "USER";
                    }
            );

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", newTokens.get("accessToken"));
            result.put("accessTokenExpire", newTokens.get("accessTokenExpire"));

            // 根据配置决定RefreshToken的返回方式
            if (useHttpOnlyCookie) {
                // 更新Cookie中的RefreshToken
                String newRefreshToken = (String) newTokens.get("refreshToken");
                int maxAge = (int) (jwtProperties.getRefreshToken().getTtl() / 1000);

                Cookie cookie = CookieUtils.createCookie(
                        jwtProperties.getRefreshToken().getTokenName(),
                        newRefreshToken,
                        maxAge,
                        true,
                        cookieSecure,
                        "/",
                        null
                );
                CookieUtils.addCookie(response, cookie);
                result.put("refreshTokenMode", "cookie");
            } else {
                result.put("refreshToken", newTokens.get("refreshToken"));
                result.put("refreshTokenExpire", newTokens.get("refreshTokenExpire"));
                result.put("refreshTokenMode", "json");
            }

            log.info("令牌刷新成功");
            return R.ok(result);
        } catch (Exception e) {
            log.error("令牌刷新失败: {}", e.getMessage());
            return R.error(401, "刷新令牌无效或已过期");
        }
    }

    /**
     * 用户登出 - 撤销RefreshToken
     *
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse（用于删除Cookie）
     * @param logoutRequest 登出请求
     * @return 登出结果
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody LogoutRequest logoutRequest) {
        log.info("用户登出请求: userId={}", logoutRequest.getUserId());

        try {
            // 撤销Redis中的RefreshToken
            tokenService.revokeRefreshToken(logoutRequest.getUserId());

            // 如果使用Cookie，删除Cookie
            if (useHttpOnlyCookie) {
                CookieUtils.deleteCookie(response, jwtProperties.getRefreshToken().getTokenName());
                log.debug("已删除RefreshToken Cookie");
            }

            log.info("用户 {} 登出成功", logoutRequest.getUserId());
            return R.ok("登出成功");
        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage());
            return R.error(500, "登出失败");
        }
    }

    /**
     * 登录请求DTO
     */
    @lombok.Data
    public static class LoginRequest {
        private String identifier; // 用户名或邮箱
        private String password;
    }

    /**
     * 刷新令牌请求DTO
     */
    @lombok.Data
    public static class RefreshRequest {
        private String refreshToken;
    }

    /**
     * 登出请求DTO
     */
    @lombok.Data
    public static class LogoutRequest {
        private Long userId;
    }
}
