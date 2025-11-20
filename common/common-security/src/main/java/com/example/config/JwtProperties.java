package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT密钥
     */
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
        private long ttl = 900000L;

        /**
         * AccessToken名称
         */
        private String tokenName = "accessToken";
    }

    @Data
    public static class RefreshToken {
        /**
         * RefreshToken有效期（毫秒），默认7天
         */
        private long ttl = 604800000L;

        /**
         * RefreshToken名称
         */
        private String tokenName = "refreshToken";

        /**
         * RefreshToken在Redis中的key前缀
         */
        private String redisKeyPrefix = "refresh_token:";
    }
}
