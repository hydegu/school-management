package com.example.gateway.filter;

import com.example.gateway.config.JwtProperties;
import com.example.gateway.config.WhiteListProperties;
import com.example.gateway.constants.GatewayConstants;
import com.example.gateway.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT认证全局过滤器 - WebFlux版本
 * 负责验证JWT Token并提取用户信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtProperties jwtProperties;
    private final WhiteListProperties whiteListProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.debug("处理请求: {}", path);

        // 1. 检查是否在白名单中
        if (isWhiteListed(path)) {
            log.debug("白名单路径，跳过认证: {}", path);
            return chain.filter(exchange);
        }

        // 2. 提取Token
        String token = extractToken(request);
        if (token == null || token.isBlank()) {
            log.warn("请求缺少认证令牌: {}", path);
            return writeUnauthorizedResponse(exchange.getResponse(), GatewayConstants.Messages.TOKEN_MISSING);
        }

        // 3. 验证Token
        try {
            Claims claims = JwtUtils.parseJWT(jwtProperties.getSecretKey(), token);

            // 4. 验证令牌类型：只接受AccessToken
            if (!JwtUtils.isTokenType(claims, JwtUtils.TOKEN_TYPE_ACCESS)) {
                log.warn("令牌类型不正确，期望AccessToken: {}", path);
                return writeUnauthorizedResponse(exchange.getResponse(), GatewayConstants.Messages.TOKEN_TYPE_ERROR);
            }

            // 5. 提取用户信息
            Long userId = JwtUtils.getUserIdFromClaims(claims);
            String username = claims.getSubject();
            String role = JwtUtils.getRoleFromClaims(claims);

            if (userId == null || username == null) {
                log.warn("令牌缺少必要的用户信息: {}", path);
                return writeUnauthorizedResponse(exchange.getResponse(), GatewayConstants.Messages.TOKEN_INFO_INCOMPLETE);
            }

            log.debug("用户认证成功: userId={}, username={}, role={}", userId, username, role);

            // 6. 将用户信息添加到请求头，传递给下游服务
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(GatewayConstants.Headers.USER_ID, userId.toString())
                    .header(GatewayConstants.Headers.USERNAME, username)
                    .header(GatewayConstants.Headers.USER_ROLE, role != null ? role : "")
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            // 7. 继续过滤器链
            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            log.warn("JWT验证失败: path={}, error={}", path, e.getMessage());
            return writeUnauthorizedResponse(exchange.getResponse(), GatewayConstants.Messages.TOKEN_INVALID);
        }
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhiteListed(String path) {
        return whiteListProperties.getUrls().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 从请求头中提取Token
     */
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(GatewayConstants.Headers.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(GatewayConstants.Headers.BEARER_PREFIX)) {
            return bearerToken.substring(GatewayConstants.Headers.BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * 写入未授权响应
     */
    private Mono<Void> writeUnauthorizedResponse(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("code", GatewayConstants.StatusCode.UNAUTHORIZED);
        result.put("message", message);
        result.put("data", null);

        try {
            byte[] bytes = objectMapper.writeValueAsString(result).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败", e);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        // 设置较高优先级，确保在其他过滤器之前执行
        return -100;
    }
}
