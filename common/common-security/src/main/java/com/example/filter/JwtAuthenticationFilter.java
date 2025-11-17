package com.example.filter;

import com.example.config.JwtProperties;
import com.example.context.UserContext;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器 - 支持双令牌机制
 * 只验证AccessToken，RefreshToken仅在刷新接口使用
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null) {
            try {
                // 解析JWT
                Claims claims = JwtUtils.parseJWT(jwtProperties.getSecretKey(), token);

                // 验证令牌类型：只接受AccessToken
                if (!JwtUtils.isTokenType(claims, JwtUtils.TOKEN_TYPE_ACCESS)) {
                    log.warn("令牌类型不正确，期望AccessToken");
                    sendUnauthorizedResponse(response, "令牌类型不正确");
                    return;
                }

                // 提取用户信息
                Long userId = JwtUtils.getUserIdFromClaims(claims);
                String username = claims.getSubject();
                String role = JwtUtils.getRoleFromClaims(claims);

                if (userId == null || username == null) {
                    log.warn("令牌缺少必要的用户信息");
                    sendUnauthorizedResponse(response, "令牌信息不完整");
                    return;
                }

                // 设置用户上下文
                UserContext.setUserId(userId.toString());
                UserContext.setUsername(username);
                UserContext.setRole(role);

                log.debug("用户认证成功: userId={}, username={}, role={}", userId, username, role);

            } catch (Exception e) {
                log.warn("JWT验证失败: {}", e.getMessage());
                sendUnauthorizedResponse(response, "Token无效或已过期");
                return;
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 从请求头中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 发送未授权响应
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\":401,\"message\":\"%s\"}", message));
    }
}