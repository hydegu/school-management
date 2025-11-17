package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * 未授权异常
 * 用于用户未登录或 Token 无效的情况
 */
public class UnauthorizedException extends ApiException {

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "未授权，请先登录");
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }
}
