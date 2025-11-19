package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 认证响应DTO（登录和刷新令牌通用）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * AccessToken（访问令牌）
     */
    private String accessToken;

    /**
     * RefreshToken（刷新令牌）- Cookie模式下为null
     */
    private String refreshToken;

    /**
     * AccessToken有效期（毫秒）
     */
    private Long accessTokenExpire;

    /**
     * RefreshToken有效期（毫秒）- Cookie模式下为null
     */
    private Long refreshTokenExpire;

    /**
     * RefreshToken模式：json 或 cookie
     */
    private String refreshTokenMode;
}
