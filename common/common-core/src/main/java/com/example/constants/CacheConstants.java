package com.example.constants;

/**
 * 缓存常量
 */
public final class CacheConstants {

    private CacheConstants() {
        // 常量类，禁止实例化
    }

    /**
     * 缓存前缀
     */
    public static final String CACHE_PREFIX = "school:";

    /**
     * 用户缓存
     */
    public static final String USER_CACHE = CACHE_PREFIX + "user:";
    public static final String USER_INFO = USER_CACHE + "info:";
    public static final String USER_PERMISSIONS = USER_CACHE + "permissions:";
    public static final String USER_ROLES = USER_CACHE + "roles:";

    /**
     * Token 缓存
     */
    public static final String TOKEN_CACHE = CACHE_PREFIX + "token:";

    /**
     * 验证码缓存
     */
    public static final String CAPTCHA_CACHE = CACHE_PREFIX + "captcha:";

    /**
     * 缓存过期时间（秒）
     */
    public static final long CACHE_EXPIRE_5_MIN = 5 * 60;
    public static final long CACHE_EXPIRE_10_MIN = 10 * 60;
    public static final long CACHE_EXPIRE_30_MIN = 30 * 60;
    public static final long CACHE_EXPIRE_1_HOUR = 60 * 60;
    public static final long CACHE_EXPIRE_1_DAY = 24 * 60 * 60;
    public static final long CACHE_EXPIRE_7_DAY = 7 * 24 * 60 * 60;
}
