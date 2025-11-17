package com.example.authservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登出请求DTO
 */
@Data
public class LogoutRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
}
