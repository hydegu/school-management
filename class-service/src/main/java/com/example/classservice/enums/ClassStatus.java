package com.example.classservice.enums;

import lombok.Getter;

/**
 * 班级状态枚举
 */
@Getter
public enum ClassStatus {
    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    private final Integer code;
    private final String desc;

    ClassStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static ClassStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ClassStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
