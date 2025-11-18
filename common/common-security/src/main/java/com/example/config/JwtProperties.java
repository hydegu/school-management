package com.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT密钥
     */
    @Value("${jwt.secret-key}")
    private String secretKey;

    /**
     * AccessToken配置
     */
    private AccessToken accessToken = new AccessToken();

    /**
     * RefreshToken配置
     */
    private RefreshToken refreshToken = new RefreshToken();

    @Data
    public static class AccessToken {
        /**
         * AccessToken有效期（毫秒），默认15分钟
         */
        @Value("${jwt.access-token.ttl}")
        private long ttl;

        /**
         * AccessToken名称
         */
        @Value("${jwt.access-token.token-name}")
        private String tokenName;
    }

    @Data
    public static class RefreshToken {
        /**
         * RefreshToken有效期（毫秒），默认7天
         */
        @Value("${jwt.refresh-token.ttl}")
        private long ttl;

        /**
         * RefreshToken名称
         */
        @Value("${jwt.refresh-token.token-name}")
        private String tokenName;

        /**
         * RefreshToken在Redis中的key前缀
         */
        @Value("${jwt.refresh-token.redis-key-prefix}")
        private String redisKeyPrefix;
    }
}
