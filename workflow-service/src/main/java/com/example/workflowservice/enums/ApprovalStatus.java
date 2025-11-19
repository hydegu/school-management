package com.example.workflowservice.enums;

import lombok.Getter;

/**
 * 审批状态枚举
 */
@Getter
public enum ApprovalStatus {

    PENDING(1, "待审批"),
    APPROVING(2, "审批中"),
    APPROVED(3, "已通过"),
    REJECTED(4, "已拒绝"),
    WITHDRAWN(5, "已撤回");

    private final Integer code;
    private final String desc;

    ApprovalStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ApprovalStatus fromCode(Integer code) {
        for (ApprovalStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
