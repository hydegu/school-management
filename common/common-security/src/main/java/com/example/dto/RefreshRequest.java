package com.example.dto;

import lombok.Data;

/**
 * 刷新令牌请求DTO
 */
@Data
public class RefreshRequest {
    private String refreshToken;
}