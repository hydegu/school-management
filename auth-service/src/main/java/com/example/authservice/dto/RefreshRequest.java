package com.example.authservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 刷新令牌请求DTO
 */
@Data
public class RefreshRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * RefreshToken（仅JSON模式需要）
     */
    private String refreshToken;
}
