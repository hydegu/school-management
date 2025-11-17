package com.example.dto;

import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {
    private String identifier; // 用户名或邮箱
    private String password;
}