package com.example.authservice.controller;

import com.example.authservice.entity.AppUser;
import com.example.authservice.entity.Role;
import com.example.authservice.service.UserService;
import com.example.service.TokenService;
import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器 - 支持双令牌机制
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户登录 - 返回AccessToken和RefreshToken
     *
     * @param loginRequest 登录请求
     * @return 双令牌
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        log.info("用户登录请求: {}", loginRequest.getIdentifier());

        // 查找用户
        AppUser user = userService.findByIdentifier(loginRequest.getIdentifier())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("用户 {} 密码错误", loginRequest.getIdentifier());
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new IllegalArgumentException("用户已被禁用");
        }

        // 获取用户角色
        Role role = userService.selectRolesByUserId(user.getId());
        String roleName = role != null ? role.getRoleName() : "USER";

        // 生成双令牌
        Map<String, Object> tokens = tokenService.generateTokens(
                user.getId().longValue(),
                user.getUserName(),
                roleName
        );

        log.info("用户 {} 登录成功", user.getUserName());
        return R.ok(tokens);
    }

    /**
     * 刷新令牌 - 使用RefreshToken获取新的双令牌
     *
     * @param refreshRequest 刷新请求
     * @return 新的双令牌
     */
    @PostMapping("/refresh")
    public R<Map<String, Object>> refresh(@RequestBody RefreshRequest refreshRequest) {
        log.info("令牌刷新请求");

        try {
            Map<String, Object> newTokens = tokenService.refreshAccessToken(refreshRequest.getRefreshToken());
            log.info("令牌刷新成功");
            return R.ok(newTokens);
        } catch (Exception e) {
            log.error("令牌刷新失败: {}", e.getMessage());
            return R.error(401, "刷新令牌无效或已过期");
        }
    }

    /**
     * 用户登出 - 撤销RefreshToken
     *
     * @param logoutRequest 登出请求
     * @return 登出结果
     */
    @PostMapping("/logout")
    public R<String> logout(@RequestBody LogoutRequest logoutRequest) {
        log.info("用户登出请求: userId={}", logoutRequest.getUserId());

        try {
            tokenService.revokeRefreshToken(logoutRequest.getUserId());
            log.info("用户 {} 登出成功", logoutRequest.getUserId());
            return R.ok("登出成功");
        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage());
            return R.error(500, "登出失败");
        }
    }

    /**
     * 登录请求DTO
     */
    @lombok.Data
    public static class LoginRequest {
        private String identifier; // 用户名或邮箱
        private String password;
    }

    /**
     * 刷新令牌请求DTO
     */
    @lombok.Data
    public static class RefreshRequest {
        private String refreshToken;
    }

    /**
     * 登出请求DTO
     */
    @lombok.Data
    public static class LogoutRequest {
        private Long userId;
    }
}
