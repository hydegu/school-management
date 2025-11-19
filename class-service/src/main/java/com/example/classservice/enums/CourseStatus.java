package com.example.classservice.enums;

import lombok.Getter;

/**
 * 课程状态枚举
 */
@Getter
public enum CourseStatus {
    DISABLED(0, "停用"),
    IN_PROGRESS(1, "进行中"),
    FINISHED(2, "已结束");

    private final Integer code;
    private final String desc;

    CourseStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static CourseStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CourseStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
