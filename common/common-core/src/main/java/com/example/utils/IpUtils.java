package com.example.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP 地址工具类
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final int IP_MAX_LENGTH = 15;

    private IpUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 获取客户端真实 IP 地址
     * 支持多级代理和负载均衡场景
     *
     * @param request HTTP 请求对象
     * @return 客户端 IP 地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // 多次反向代理后会有多个 IP 值，第一个为真实 IP
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index).trim();
            }
            return ip.trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip.trim();
        }

        ip = request.getRemoteAddr();
        return ip != null ? ip.trim() : UNKNOWN;
    }

    /**
     * 验证 IP 地址是否有效
     *
     * @param ip IP 地址
     * @return true 表示有效，false 表示无效
     */
    private static boolean isValidIp(String ip) {
        return ip != null
            && !ip.isBlank()
            && !UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 判断是否为本地地址
     *
     * @param ip IP 地址
     * @return true 表示是本地地址
     */
    public static boolean isLocalhost(String ip) {
        return LOCALHOST_IPV4.equals(ip) || LOCALHOST_IPV6.equals(ip);
    }

    /**
     * 将 IPv6 地址转换为 IPv4 格式（如果是本地地址）
     *
     * @param ip IP 地址
     * @return 转换后的 IP 地址
     */
    public static String normalizeLocalhost(String ip) {
        if (LOCALHOST_IPV6.equals(ip)) {
            return LOCALHOST_IPV4;
        }
        return ip;
    }

    /**
     * 验证 IP 地址格式是否正确（IPv4）
     *
     * @param ip IP 地址
     * @return true 表示格式正确
     */
    public static boolean isValidIPv4(String ip) {
        if (ip == null || ip.isBlank()) {
            return false;
        }

        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        try {
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}