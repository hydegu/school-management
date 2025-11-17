package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 业务异常
 * 用于业务逻辑中抛出的异常，会被全局异常处理器捕获并转换为统一响应
 */
@Getter
public class ApiException extends RuntimeException {

    /**
     * HTTP 状态码
     */
    private final HttpStatus status;

    /**
     * 业务错误码
     */
    private final Integer code;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.code = status.value();
    }

    public ApiException(HttpStatus status, Integer code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public ApiException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public ApiException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = status.value();
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    // 便捷静态方法

    public static ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }

    public static ApiException unauthorized(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, message);
    }

    public static ApiException forbidden(String message) {
        return new ApiException(HttpStatus.FORBIDDEN, message);
    }

    public static ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, message);
    }

    public static ApiException conflict(String message) {
        return new ApiException(HttpStatus.CONFLICT, message);
    }

    public static ApiException gone(String message) {
        return new ApiException(HttpStatus.GONE, message);
    }

    public static ApiException internalError(String message) {
        return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
