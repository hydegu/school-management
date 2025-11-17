package com.example.utils;

import java.security.SecureRandom;

/**
 * 验证码生成工具类
 */
public class CodeUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC_CHARS = "0123456789";
    private static final String ALPHABETIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private CodeUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 生成默认长度(6位)的字母数字混合验证码
     * @return 验证码字符串
     */
    public static String generateCode() {
        return generateCode(6);
    }

    /**
     * 生成指定长度的字母数字混合验证码
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String generateCode(int length) {
        return generateCode(length, ALPHANUMERIC_CHARS);
    }

    /**
     * 生成指定长度的纯数字验证码
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String generateNumericCode(int length) {
        return generateCode(length, NUMERIC_CHARS);
    }

    /**
     * 生成指定长度的纯字母验证码
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String generateAlphabeticCode(int length) {
        return generateCode(length, ALPHABETIC_CHARS);
    }

    /**
     * 生成指定长度的验证码，从给定字符集中选择
     * @param length 验证码长度
     * @param chars 字符集
     * @return 验证码字符串
     */
    public static String generateCode(int length, String chars) {
        if (length <= 0) {
            throw new IllegalArgumentException("验证码长度必须大于0");
        }
        if (chars == null || chars.isEmpty()) {
            throw new IllegalArgumentException("字符集不能为空");
        }

        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }
}