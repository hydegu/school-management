package com.example.constants;

/**
 * 通用常量类
 */
public final class CommonConstants {

    private CommonConstants() {
        // 常量类，禁止实例化
    }

    /**
     * 字符常量
     */
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String UNDERLINE = "_";
    public static final String HYPHEN = "-";
    public static final String SPACE = " ";

    /**
     * 通用状态
     */
    public static final Integer STATUS_NORMAL = 0;
    public static final Integer STATUS_DISABLED = 1;

    /**
     * 删除标志
     */
    public static final Integer NOT_DELETED = 0;
    public static final Integer DELETED = 1;

    /**
     * 是/否
     */
    public static final Integer YES = 1;
    public static final Integer NO = 0;

    /**
     * 成功/失败
     */
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    /**
     * 编码
     */
    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";

    /**
     * HTTP 相关
     */
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";

    /**
     * Token 相关
     */
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_KEY = "token";

    /**
     * 用户相关
     */
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String USER_INFO = "userInfo";

    /**
     * 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 500;
}
