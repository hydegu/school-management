package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源未找到异常
 */
public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(HttpStatus.NOT_FOUND, String.format("%s 未找到，ID: %s", resourceName, resourceId));
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }
}
