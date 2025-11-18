package com.example.businessservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 确认状态枚举（用于换课申请中的对方确认）
 */
@Getter
@AllArgsConstructor
public enum ConfirmStatus {

    /**
     * 未确认
     */
    UNCONFIRMED(0, "未确认"),

    /**
     * 已确认
     */
    CONFIRMED(1, "已确认"),

    /**
     * 已拒绝
     */
    REJECTED(2, "已拒绝");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 状态码
     * @return 枚举对象
     */
    public static ConfirmStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ConfirmStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
