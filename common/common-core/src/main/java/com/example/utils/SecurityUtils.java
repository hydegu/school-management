package com.example.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security 工具类
 */
public final class SecurityUtils {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private SecurityUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 获取当前认证对象
     *
     * @return Optional<Authentication>
     */
    public static Optional<Authentication> getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(authentication);
    }

    /**
     * 获取当前用户名
     *
     * @return Optional<String>
     */
    public static Optional<String> getCurrentUsername() {
        return getAuthentication().map(authentication -> {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }
            if (principal instanceof String str) {
                return str;
            }
            return null;
        });
    }

    /**
     * 获取当前用户详情
     *
     * @return Optional<UserDetails>
     */
    public static Optional<UserDetails> getCurrentUserDetails() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserDetails)
                .map(principal -> (UserDetails) principal);
    }

    /**
     * 获取当前用户的所有权限
     *
     * @return 权限集合
     */
    public static Set<String> getCurrentAuthorities() {
        return getAuthentication()
                .map(Authentication::getAuthorities)
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    /**
     * 检查当前用户是否拥有指定权限
     *
     * @param authority 权限标识
     * @return true 表示拥有该权限
     */
    public static boolean hasAuthority(String authority) {
        if (authority == null || authority.isBlank()) {
            return false;
        }
        return getCurrentAuthorities().contains(authority);
    }

    /**
     * 检查当前用户是否拥有指定角色
     *
     * @param role 角色名称（不需要 ROLE_ 前缀）
     * @return true 表示拥有该角色
     */
    public static boolean hasRole(String role) {
        if (role == null || role.isBlank()) {
            return false;
        }
        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return hasAuthority(roleWithPrefix);
    }

    /**
     * 检查当前用户是否拥有任意一个指定权限
     *
     * @param authorities 权限列表
     * @return true 表示拥有任意一个权限
     */
    public static boolean hasAnyAuthority(String... authorities) {
        if (authorities == null || authorities.length == 0) {
            return false;
        }
        Set<String> currentAuthorities = getCurrentAuthorities();
        for (String authority : authorities) {
            if (currentAuthorities.contains(authority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查当前用户是否拥有所有指定权限
     *
     * @param authorities 权限列表
     * @return true 表示拥有所有权限
     */
    public static boolean hasAllAuthorities(String... authorities) {
        if (authorities == null || authorities.length == 0) {
            return false;
        }
        Set<String> currentAuthorities = getCurrentAuthorities();
        for (String authority : authorities) {
            if (!currentAuthorities.contains(authority)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查当前用户是否已认证
     *
     * @return true 表示已认证
     */
    public static boolean isAuthenticated() {
        return getAuthentication().isPresent();
    }

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return true 表示密码匹配
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    /**
     * 获取密码编码器
     *
     * @return PasswordEncoder
     */
    public static PasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }
}
