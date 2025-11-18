package com.example.service;

import com.example.config.JwtProperties;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate redisTemplate;

    @Override
    public Map<String, Object> generateTokens(Long userId, String username, String role) {
        // 生成AccessToken
        String accessToken = JwtUtils.createAccessToken(
                jwtProperties.getSecretKey(),
                jwtProperties.getAccessToken().getTtl(),
                userId,
                username,
                role
        );

        // 生成RefreshToken
        String refreshToken = JwtUtils.createRefreshToken(
                jwtProperties.getSecretKey(),
                jwtProperties.getRefreshToken().getTtl(),
                userId,
                username
        );

        // 将RefreshToken存储到Redis
        String redisKey = getRefreshTokenKey(userId);
        long ttlSeconds = jwtProperties.getRefreshToken().getTtl() / 1000;
        redisTemplate.opsForValue().set(redisKey, refreshToken, ttlSeconds, TimeUnit.SECONDS);

        log.info("为用户 {} 生成双令牌成功", username);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("accessTokenExpire", jwtProperties.getAccessToken().getTtl());
        tokens.put("refreshTokenExpire", jwtProperties.getRefreshToken().getTtl());
        return tokens;
    }

    /**
     * 刷新AccessToken（需要提供roleProvider获取最新角色）
     *
     * @param refreshToken RefreshToken
     * @param roleProvider 角色提供者（根据userId获取最新角色）
     * @return 新的双令牌
     * @throws IllegalArgumentException 如果RefreshToken无效
     */

    @Override
    public Map<String, Object> refreshAccessToken(String refreshToken, TokenService.RoleProvider roleProvider) {
        try {
            // 解析RefreshToken
            Claims claims = JwtUtils.parseJWT(jwtProperties.getSecretKey(), refreshToken);

            // 验证令牌类型
            if (!JwtUtils.isTokenType(claims, JwtUtils.TOKEN_TYPE_REFRESH)) {
                throw new IllegalArgumentException("令牌类型不正确，期望RefreshToken");
            }

            // 提取用户信息
            Long userId = JwtUtils.getUserIdFromClaims(claims);
            String username = claims.getSubject();

            if (userId == null || username == null) {
                throw new IllegalArgumentException("RefreshToken缺少必要信息");
            }

            // 验证Redis中的RefreshToken是否存在且匹配
            String redisKey = getRefreshTokenKey(userId);
            String storedToken = redisTemplate.opsForValue().get(redisKey);

            if (storedToken == null) {
                throw new IllegalArgumentException("RefreshToken已失效或不存在");
            }

            if (!storedToken.equals(refreshToken)) {
                log.warn("RefreshToken不匹配，可能存在安全风险。userId={}", userId);
                throw new IllegalArgumentException("RefreshToken不匹配");
            }

            // 从roleProvider获取用户最新的角色信息
            String role = roleProvider != null ? roleProvider.getRole(userId) : null;
            if (role == null) {
                log.warn("无法获取用户 {} 的角色信息，使用默认角色", userId);
                role = "student";  // 默认角色
            }

            // 生成新的双令牌
            Map<String, Object> newTokens = generateTokens(userId, username, role);

            log.info("用户 {} 刷新令牌成功，角色: {}", username, role);
            return newTokens;

        } catch (Exception e) {
            log.error("刷新令牌失败: {}", e.getMessage());
            throw new IllegalArgumentException("RefreshToken无效: " + e.getMessage());
        }
    }


    /**
     * 撤销RefreshToken（用于登出）
     *
     * @param userId 用户ID
     */
    @Override
    public void revokeRefreshToken(Long userId) {
        String redisKey = getRefreshTokenKey(userId);
        redisTemplate.delete(redisKey);
        log.info("用户 {} 的RefreshToken已撤销", userId);
    }

    /**
     * 验证RefreshToken是否有效
     *
     * @param userId       用户ID
     * @param refreshToken RefreshToken
     * @return true表示有效
     */
    @Override
    public boolean validateRefreshToken(Long userId, String refreshToken) {
        String redisKey = getRefreshTokenKey(userId);
        String storedToken = redisTemplate.opsForValue().get(redisKey);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    /**
     * 获取RefreshToken的Redis Key
     */
    private String getRefreshTokenKey(Long userId) {
        return jwtProperties.getRefreshToken().getRedisKeyPrefix() + userId;
    }
}
