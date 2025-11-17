package com.example.utils;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^1[3-9]\\d{9}$"
    );

    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
            "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$"
    );

    private StringUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return true 表示为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return true 表示不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白（包括空格、制表符等）
     *
     * @param str 字符串
     * @return true 表示为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 字符串
     * @return true 表示不为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 去除字符串两端空格，null 返回 null
     *
     * @param str 字符串
     * @return 去除空格后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 去除字符串两端空格，null 返回空字符串
     *
     * @param str 字符串
     * @return 去除空格后的字符串
     */
    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 去除字符串两端空格，空字符串返回 null
     *
     * @param str 字符串
     * @return 去除空格后的字符串
     */
    public static String trimToNull(String str) {
        String trimmed = trim(str);
        return isEmpty(trimmed) ? null : trimmed;
    }

    /**
     * 获取默认值，如果字符串为空则返回默认值
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 字符串或默认值
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * 获取默认值，如果字符串为空白则返回默认值
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return 字符串或默认值
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    /**
     * 连接字符串数组
     *
     * @param separator 分隔符
     * @param elements  字符串数组
     * @return 连接后的字符串
     */
    public static String join(String separator, String... elements) {
        if (elements == null || elements.length == 0) {
            return "";
        }
        return String.join(separator, elements);
    }

    /**
     * 连接字符串集合
     *
     * @param separator 分隔符
     * @param elements  字符串集合
     * @return 连接后的字符串
     */
    public static String join(String separator, Collection<String> elements) {
        if (elements == null || elements.isEmpty()) {
            return "";
        }
        return String.join(separator, elements);
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写的字符串
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写的字符串
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 驼峰转下划线
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String camelToUnderscore(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    /**
     * 下划线转驼峰
     *
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String underscoreToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        boolean toUpper = false;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                toUpper = true;
            } else {
                result.append(toUpper ? Character.toUpperCase(c) : c);
                toUpper = false;
            }
        }
        return result.toString();
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return true 表示格式正确
     */
    public static boolean isEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式（中国大陆）
     *
     * @param phone 手机号
     * @return true 表示格式正确
     */
    public static boolean isPhone(String phone) {
        if (isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证身份证号格式（中国大陆 18 位）
     *
     * @param idCard 身份证号
     * @return true 表示格式正确
     */
    public static boolean isIdCard(String idCard) {
        if (isBlank(idCard)) {
            return false;
        }
        return ID_CARD_PATTERN.matcher(idCard).matches();
    }

    /**
     * 脱敏手机号（保留前3位和后4位）
     *
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (isBlank(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 脱敏邮箱（保留前2位和@后面的内容）
     *
     * @param email 邮箱地址
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) {
            return email;
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }

    /**
     * 脱敏身份证号（保留前6位和后4位）
     *
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (isBlank(idCard) || idCard.length() != 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }
}
