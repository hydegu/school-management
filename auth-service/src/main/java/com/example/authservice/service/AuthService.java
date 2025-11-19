package com.example.authservice.service;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param request  登录请求
     * @param response HttpServletResponse（用于设置Cookie）
     * @return 认证响应（包含令牌）
     */
    AuthResponse login(LoginRequest request, HttpServletResponse response);

    /**
     * 刷新令牌
     *
     * @param refreshToken RefreshToken（可选，Cookie模式下从Cookie获取）
     * @param httpRequest  HttpServletRequest（用于读取Cookie）
     * @param httpResponse HttpServletResponse（用于设置Cookie）
     * @return 认证响应（新的令牌）
     */
    AuthResponse refreshToken(String refreshToken, HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    /**
     * 用户登出
     *
     * @param userId       用户ID
     * @param httpResponse HttpServletResponse（用于删除Cookie）
     */
    void logout(Long userId, HttpServletResponse httpResponse);
}
