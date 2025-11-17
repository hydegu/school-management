package com.example.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

/**
 * Cookie工具类 - 用于HttpOnly Cookie管理
 */
@Slf4j
public class CookieUtils {

    private CookieUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 创建HttpOnly Cookie
     *
     * @param name     Cookie名称
     * @param value    Cookie值
     * @param maxAge   有效期（秒），-1表示会话级别
     * @param httpOnly 是否HttpOnly
     * @param secure   是否仅HTTPS（生产环境建议true）
     * @param path     Cookie路径
     * @param domain   Cookie域名
     * @return Cookie对象
     */
    public static Cookie createCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure, String path, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(path != null ? path : "/");

        if (domain != null && !domain.isEmpty()) {
            cookie.setDomain(domain);
        }

        // SameSite属性（防止CSRF）
        // 注意：Jakarta Servlet 6.0+支持，低版本需要手动设置响应头
        // cookie.setAttribute("SameSite", "Strict");

        return cookie;
    }

    /**
     * 创建HttpOnly Cookie（简化版）
     *
     * @param name   Cookie名称
     * @param value  Cookie值
     * @param maxAge 有效期（秒）
     * @return Cookie对象
     */
    public static Cookie createHttpOnlyCookie(String name, String value, int maxAge) {
        return createCookie(name, value, maxAge, true, false, "/", null);
    }

    /**
     * 创建安全的HttpOnly Cookie（生产环境使用）
     *
     * @param name   Cookie名称
     * @param value  Cookie值
     * @param maxAge 有效期（秒）
     * @return Cookie对象
     */
    public static Cookie createSecureCookie(String name, String value, int maxAge) {
        return createCookie(name, value, maxAge, true, true, "/", null);
    }

    /**
     * 添加Cookie到响应
     *
     * @param response HttpServletResponse
     * @param cookie   Cookie对象
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
        log.debug("添加Cookie: name={}, maxAge={}, httpOnly={}, secure={}",
                cookie.getName(), cookie.getMaxAge(), cookie.isHttpOnly(), cookie.getSecure());
    }

    /**
     * 从请求中获取Cookie值
     *
     * @param request    HttpServletRequest
     * @param cookieName Cookie名称
     * @return Optional<String> Cookie值
     */
    public static Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .map(Cookie::getValue)
                .findFirst();
    }

    /**
     * 删除Cookie
     *
     * @param response   HttpServletResponse
     * @param cookieName Cookie名称
     */
    public static void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        log.debug("删除Cookie: name={}", cookieName);
    }

    /**
     * 删除Cookie（指定路径和域名）
     *
     * @param response   HttpServletResponse
     * @param cookieName Cookie名称
     * @param path       Cookie路径
     * @param domain     Cookie域名
     */
    public static void deleteCookie(HttpServletResponse response, String cookieName, String path, String domain) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(path != null ? path : "/");
        if (domain != null && !domain.isEmpty()) {
            cookie.setDomain(domain);
        }
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        log.debug("删除Cookie: name={}, path={}, domain={}", cookieName, path, domain);
    }

    /**
     * 检查Cookie是否存在
     *
     * @param request    HttpServletRequest
     * @param cookieName Cookie名称
     * @return true表示存在
     */
    public static boolean hasCookie(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName).isPresent();
    }
}
