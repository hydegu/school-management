package com.example.exception;

import com.example.enums.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 * 用于业务逻辑中的异常情况，不会记录完整堆栈
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.ERROR.getCode();
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 重写 fillInStackTrace 方法以提高性能
     * 业务异常通常不需要完整的堆栈信息
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
