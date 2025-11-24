package com.example.authservice.controller;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RefreshRequest;
import com.example.authservice.service.AuthService;
import com.example.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 负责接收请求和返回响应
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     *
     * @param request  登录请求
     * @param response HttpServletResponse
     * @return 认证响应
     */
    @PostMapping("/login")
    public R<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.login(request, response);
            return R.ok(authResponse);
        }  catch (IllegalArgumentException e) {
            log.warn("登录失败: {}", e.getMessage());
            return R.error(401, e.getMessage());
        } catch (Exception e) {
            log.error("登录异常: ", e);
            return R.error(500, "登录失败");
        }
    }

    /**
     * 刷新令牌
     *
     * @param refreshRequest 刷新请求（可选，Cookie模式下不需要）
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @return 认证响应
     */
    @PostMapping("/refresh")
    public R<AuthResponse> refresh(@RequestBody(required = false) RefreshRequest refreshRequest,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            String refreshToken = refreshRequest != null ? refreshRequest.getRefreshToken() : null;
            AuthResponse authResponse = authService.refreshToken(refreshToken, request, response);
            return R.ok(authResponse);
        } catch (IllegalArgumentException e) {
            log.warn("刷新令牌失败: {}", e.getMessage());
            return R.error(401, e.getMessage());
        } catch (Exception e) {
            log.error("刷新令牌异常: ", e);
            return R.error(500, "刷新令牌失败");
        }
    }

    /**
     * 用户登出
     *
     * @param response HttpServletResponse
     * @return 登出结果
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletResponse response) {
        try {
            authService.logout(response);
            return R.ok("登出成功");
        } catch (Exception e) {
            log.error("登出异常: ", e);
            return R.error(500, "登出失败");
        }
    }
}
