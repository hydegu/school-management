package com.example.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * JWT 工具类
 */
@Slf4j
public class JwtUtils {

    private JwtUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 生成 JWT Token
     * 使用 HS256 算法
     *
     * @param secretKey  JWT 密钥
     * @param ttlMillis  有效期（毫秒）
     * @param claims     自定义声明
     * @return JWT Token
     */
    public static String createJwt(String secretKey, long ttlMillis, Map<String, Object> claims) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        if (ttlMillis <= 0) {
            throw new IllegalArgumentException("有效期必须大于0");
        }

        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        JwtBuilder builder = Jwts.builder()
                .signWith(key)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(exp);

        return builder.compact();
    }

    /**
     * 生成 JWT Token（带主题）
     *
     * @param secretKey  JWT 密钥
     * @param ttlMillis  有效期（毫秒）
     * @param subject    主题（通常是用户标识）
     * @param claims     自定义声明
     * @return JWT Token
     */
    public static String createJwt(String secretKey, long ttlMillis, String subject, Map<String, Object> claims) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        if (ttlMillis <= 0) {
            throw new IllegalArgumentException("有效期必须大于0");
        }

        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        JwtBuilder builder = Jwts.builder()
                .signWith(key)
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(exp);

        return builder.compact();
    }

    /**
     * 解析 JWT Token
     *
     * @param secretKey JWT 密钥
     * @param token     JWT Token
     * @return Claims 对象
     * @throws JwtException Token 解析失败
     */
    public static Claims parseJWT(String secretKey, String token) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token不能为空");
        }

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(key)
                .build();

        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
        return jws.getPayload();
    }

    /**
     * 安全地解析 JWT Token，返回 Optional
     *
     * @param secretKey JWT 密钥
     * @param token     JWT Token
     * @return Optional<Claims>
     */
    public static Optional<Claims> parseJWTSafely(String secretKey, String token) {
        try {
            Claims claims = parseJWT(secretKey, token);
            return Optional.of(claims);
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token 已过期: {}", e.getMessage());
            return Optional.empty();
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的 JWT Token: {}", e.getMessage());
            return Optional.empty();
        } catch (MalformedJwtException e) {
            log.warn("JWT Token 格式错误: {}", e.getMessage());
            return Optional.empty();
        } catch (SignatureException e) {
            log.warn("JWT Token 签名验证失败: {}", e.getMessage());
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.warn("JWT Token 参数错误: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 验证 Token 是否有效
     *
     * @param secretKey JWT 密钥
     * @param token     JWT Token
     * @return true 表示有效
     */
    public static boolean validateToken(String secretKey, String token) {
        return parseJWTSafely(secretKey, token).isPresent();
    }

    /**
     * 从 Token 中获取用户标识（Subject）
     *
     * @param secretKey JWT 密钥
     * @param token     JWT Token
     * @return 用户标识
     */
    public static Optional<String> getSubject(String secretKey, String token) {
        return parseJWTSafely(secretKey, token)
                .map(Claims::getSubject);
    }

    /**
     * 检查 Token 是否即将过期
     *
     * @param secretKey   JWT 密钥
     * @param token       JWT Token
     * @param thresholdMs 阈值（毫秒），小于此值认为即将过期
     * @return true 表示即将过期
     */
    public static boolean isTokenExpiringSoon(String secretKey, String token, long thresholdMs) {
        return parseJWTSafely(secretKey, token)
                .map(claims -> {
                    Date expiration = claims.getExpiration();
                    long timeToExpire = expiration.getTime() - System.currentTimeMillis();
                    return timeToExpire < thresholdMs;
                })
                .orElse(true);
    }
}
