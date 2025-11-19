package com.example.classservice.enums;

import lombok.Getter;

/**
 * 星期枚举
 */
@Getter
public enum WeekDay {
    MONDAY(1, "周一"),
    TUESDAY(2, "周二"),
    WEDNESDAY(3, "周三"),
    THURSDAY(4, "周四"),
    FRIDAY(5, "周五"),
    SATURDAY(6, "周六"),
    SUNDAY(7, "周日");

    private final Integer code;
    private final String desc;

    WeekDay(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static WeekDay getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (WeekDay day : values()) {
            if (day.getCode().equals(code)) {
                return day;
            }
        }
        return null;
    }
}
