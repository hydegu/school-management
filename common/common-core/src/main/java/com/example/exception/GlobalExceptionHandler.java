package com.example.exception;

import com.example.enums.ResultCode;
import com.example.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理各种异常并返回规范的响应格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<R<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = "请求参数校验失败";
        }

        log.warn("参数校验失败: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest(message));
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<R<Void>> handleBindException(BindException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("数据绑定失败: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest(message));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<R<Void>> handleMissingParameter(MissingServletRequestParameterException ex) {
        String message = String.format("缺少必需参数: %s", ex.getParameterName());
        log.warn(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest(message));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<R<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数类型不匹配: %s 应为 %s 类型",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知");
        log.warn(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest(message));
    }

    /**
     * 处理 HTTP 消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<R<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("HTTP 消息不可读: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest("请求体格式错误"));
    }

    /**
     * 处理不支持的 HTTP 方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = String.format("不支持的请求方法: %s", ex.getMethod());
        log.warn(message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(R.error(HttpStatus.METHOD_NOT_ALLOWED.value(), message));
    }

    /**
     * 处理不支持的媒体类型异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<R<Void>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String message = String.format("不支持的媒体类型: %s", ex.getContentType());
        log.warn(message);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(R.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), message));
    }

    /**
     * 处理找不到处理器异常（404）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<R<Void>> handleNoHandlerFound(NoHandlerFoundException ex) {
        String message = String.format("接口不存在: %s %s", ex.getHttpMethod(), ex.getRequestURL());
        log.warn(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(R.notFound(message));
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<R<Void>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("访问被拒绝: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(R.forbidden("访问被拒绝"));
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<Void>> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: code={}, message={}", ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK)
                .body(R.error(ex.getCode(), ex.getMessage()));
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<R<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("资源未找到: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(R.notFound(ex.getMessage()));
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<R<Void>> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("未授权访问: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(R.unauthorized(ex.getMessage()));
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<R<Void>> handleForbiddenException(ForbiddenException ex) {
        log.warn("权限不足: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(R.forbidden(ex.getMessage()));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<R<Void>> handleValidationException(ValidationException ex) {
        log.warn("参数验证失败: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest(ex.getMessage()));
    }

    /**
     * 处理 API 异常
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<R<Void>> handleApiException(ApiException ex) {
        HttpStatus status = ex.getStatus();
        log.warn("API 异常: status={}, code={}, message={}",
                status.value(), ex.getCode(), ex.getMessage());
        return ResponseEntity.status(status)
                .body(R.error(ex.getCode(), ex.getMessage()));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<R<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("非法参数: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(R.badRequest(ex.getMessage()));
    }

    /**
     * 处理非法状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<R<Void>> handleIllegalState(IllegalStateException ex) {
        log.warn("非法状态: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(R.conflict(ex.getMessage()));
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> handleOtherException(Exception ex) {
        log.error("系统异常", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(R.error(ResultCode.ERROR.getCode(), "系统异常，请稍后重试"));
    }
}
