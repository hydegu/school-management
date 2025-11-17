package com.example.utils;

import com.example.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果类
 * 使用 Jackson 进行 JSON 序列化
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(ResultCode rs) {
        this.code = rs.getCode();
        this.msg = rs.getMsg();
    }

    public R(ResultCode rs, T data) {
        this.code = rs.getCode();
        this.msg = rs.getMsg();
        this.data = data;
    }

    // ==================== 成功响应 ====================

    /**
     * 成功响应（无数据）
     */
    public static <T> R<T> ok() {
        return new R<>(ResultCode.SUCCESS);
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     */
    public static <T> R<T> ok(T data) {
        return new R<>(ResultCode.SUCCESS, data);
    }

    /**
     * 成功响应（自定义消息）
     *
     * @param msg 响应消息
     */
    public static <T> R<T> ok(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 成功响应（自定义消息和数据）
     *
     * @param msg  响应消息
     * @param data 响应数据
     */
    public static <T> R<T> ok(String msg, T data) {
        R<T> result = new R<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // ==================== 失败响应 ====================

    /**
     * 失败响应（默认错误消息）
     */
    public static <T> R<T> error() {
        return new R<>(ResultCode.ERROR);
    }

    /**
     * 失败响应（自定义消息）
     *
     * @param msg 错误消息
     */
    public static <T> R<T> error(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.ERROR.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 失败响应（自定义错误码和消息）
     *
     * @param code 错误码
     * @param msg  错误消息
     */
    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg);
    }

    /**
     * 失败响应（使用 ResultCode）
     *
     * @param resultCode 结果码枚举
     */
    public static <T> R<T> error(ResultCode resultCode) {
        return new R<>(resultCode);
    }

    /**
     * 失败响应（使用 ResultCode 和自定义消息）
     *
     * @param resultCode 结果码枚举
     * @param msg        自定义消息
     */
    public static <T> R<T> error(ResultCode resultCode, String msg) {
        R<T> result = new R<>();
        result.setCode(resultCode.getCode());
        result.setMsg(msg);
        return result;
    }

    // ==================== 特定场景响应 ====================

    /**
     * 未授权响应
     */
    public static <T> R<T> unauthorized() {
        return new R<>(ResultCode.UNAUTHORIZED);
    }

    /**
     * 未授权响应（自定义消息）
     */
    public static <T> R<T> unauthorized(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.UNAUTHORIZED.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 权限不足响应
     */
    public static <T> R<T> forbidden() {
        return new R<>(ResultCode.UNAUTHORIZATION);
    }

    /**
     * 权限不足响应（自定义消息）
     */
    public static <T> R<T> forbidden(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.UNAUTHORIZATION.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 资源未找到响应
     */
    public static <T> R<T> notFound() {
        return new R<>(ResultCode.NOT_FOUND);
    }

    /**
     * 资源未找到响应（自定义消息）
     */
    public static <T> R<T> notFound(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.NOT_FOUND.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 请求参数错误响应
     */
    public static <T> R<T> badRequest() {
        return new R<>(ResultCode.BAD_REQUEST);
    }

    /**
     * 请求参数错误响应（自定义消息）
     */
    public static <T> R<T> badRequest(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.BAD_REQUEST.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * 资源冲突响应
     */
    public static <T> R<T> conflict() {
        return new R<>(ResultCode.CONFLICT);
    }

    /**
     * 资源冲突响应（自定义消息）
     */
    public static <T> R<T> conflict(String msg) {
        R<T> result = new R<>();
        result.setCode(ResultCode.CONFLICT.getCode());
        result.setMsg(msg);
        return result;
    }

    // ==================== 链式调用方法 ====================

    /**
     * 设置响应数据（链式调用）
     *
     * @param data 响应数据
     * @return 当前对象
     */
    public R<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置响应消息（链式调用）
     *
     * @param msg 响应消息
     * @return 当前对象
     */
    public R<T> message(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 设置响应码（链式调用）
     *
     * @param code 响应码
     * @return 当前对象
     */
    public R<T> code(int code) {
        this.code = code;
        return this;
    }

    // ==================== 便捷判断方法 ====================

    /**
     * 判断是否成功
     *
     * @return true 表示成功
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode() == this.code;
    }

    /**
     * 判断是否失败
     *
     * @return true 表示失败
     */
    public boolean isError() {
        return !isSuccess();
    }

    // ==================== 兼容旧方法（已废弃） ====================

    /**
     * @deprecated 使用 {@link #error()} 替代
     */
    @Deprecated
    public static <T> R<T> er() {
        return error();
    }

    /**
     * @deprecated 使用 {@link #error(int, String)} 替代
     */
    @Deprecated
    public static <T> R<T> er(int code, String msg) {
        return error(code, msg);
    }

    /**
     * @deprecated 使用 {@link #forbidden()} 替代
     */
    @Deprecated
    public static <T> R<T> unauthorzation() {
        return forbidden();
    }

    /**
     * @deprecated 使用 {@link #error(String)} 替代
     */
    @Deprecated
    public static <T> R<T> checkError() {
        return error(ResultCode.USERCHECK);
    }

    /**
     * 获取数据（兼容旧字段名）
     */
    public T getDataset() {
        return data;
    }

    /**
     * 设置数据（兼容旧字段名）
     */
    public void setDataset(T data) {
        this.data = data;
    }
}
