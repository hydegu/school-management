package com.example.gateway.constants;

/**
 * 网关常量类
 */
public class GatewayConstants {

    private GatewayConstants() {
        // 常量类，禁止实例化
    }

    /**
     * 请求头常量
     */
    public static class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String USER_ID = "X-User-Id";
        public static final String USERNAME = "X-Username";
        public static final String USER_ROLE = "X-User-Role";
    }

    /**
     * Reactor Context 键常量
     */
    public static class ContextKeys {
        public static final String USER_ID = "userId";
        public static final String USERNAME = "username";
        public static final String ROLE = "role";
    }

    /**
     * 响应消息常量
     */
    public static class Messages {
        public static final String TOKEN_MISSING = "缺少认证令牌";
        public static final String TOKEN_INVALID = "令牌无效或已过期";
        public static final String TOKEN_TYPE_ERROR = "令牌类型不正确";
        public static final String TOKEN_INFO_INCOMPLETE = "令牌信息不完整";
        public static final String UNAUTHORIZED = "未授权访问";
        public static final String FORBIDDEN = "禁止访问";
        public static final String SERVICE_UNAVAILABLE = "服务暂时不可用";
    }

    /**
     * HTTP状态码常量
     */
    public static class StatusCode {
        public static final int OK = 200;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int SERVICE_UNAVAILABLE = 503;
        public static final int GATEWAY_TIMEOUT = 504;
    }
}
