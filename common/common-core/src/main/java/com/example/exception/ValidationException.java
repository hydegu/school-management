package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * 参数验证异常
 * 用于请求参数验证失败的情况
 */
public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public ValidationException(String fieldName, String errorMessage) {
        super(HttpStatus.BAD_REQUEST, String.format("字段 %s 验证失败：%s", fieldName, errorMessage));
    }

    public ValidationException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
