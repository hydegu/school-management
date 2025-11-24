package com.example.service;

import com.example.config.JwtProperties;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Token服务 - 管理AccessToken和RefreshToken
 */
public interface TokenService {

    /**
     * 生成双令牌（AccessToken + RefreshToken）
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param role     用户角色
     * @return 包含accessToken和refreshToken的Map
     */
    public Map<String, Object> generateTokens(Long userId, String username, List<String> role);

    /**
     * 刷新AccessToken（需要提供roleProvider获取最新角色）
     *
     * @param refreshToken RefreshToken
     * @param roleProvider 角色提供者（根据userId获取最新角色）
     * @return 新的双令牌
     * @throws IllegalArgumentException 如果RefreshToken无效
     */
    public Map<String, Object> refreshAccessToken(String refreshToken, RoleProvider roleProvider);

    /**
     * 角色提供者接口 - 用于刷新令牌时获取最新角色
     */
    @FunctionalInterface
    public interface RoleProvider {
        /**
         * 根据用户ID获取角色
         * @param userId 用户ID
         * @return 角色名称
         */
        List<String> getRole(Long userId);
    }

    /**
     * 撤销RefreshToken（用于登出）
     *
     * @param userId 用户ID
     */
    public void revokeRefreshToken(Long userId);

    /**
     * 验证RefreshToken是否有效
     *
     * @param userId       用户ID
     * @param refreshToken RefreshToken
     * @return true表示有效
     */
    public boolean validateRefreshToken(Long userId, String refreshToken);
}
