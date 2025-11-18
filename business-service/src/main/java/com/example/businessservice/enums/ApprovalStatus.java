package com.example.businessservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批状态枚举
 */
@Getter
@AllArgsConstructor
public enum ApprovalStatus {

    /**
     * 待审批
     */
    PENDING(1, "待审批"),

    /**
     * 已通过
     */
    APPROVED(2, "已通过"),

    /**
     * 已拒绝
     */
    REJECTED(3, "已拒绝"),

    /**
     * 已撤回
     */
    CANCELLED(4, "已撤回");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 状态码
     * @return 枚举对象
     */
    public static ApprovalStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ApprovalStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
