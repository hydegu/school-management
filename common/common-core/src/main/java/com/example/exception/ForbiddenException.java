package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * 权限不足异常
 * 用于用户已登录但无权访问资源的情况
 */
public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "权限不足，无法访问该资源");
    }

    public ForbiddenException(String message, Throwable cause) {
        super(HttpStatus.FORBIDDEN, message, cause);
    }
}
