package com.example.authservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识（用户名或邮箱）
     */
    private String identifier;

    /**
     * 密码
     */
    private String password;
}
