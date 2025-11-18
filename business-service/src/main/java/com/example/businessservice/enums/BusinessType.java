package com.example.businessservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型枚举
 */
@Getter
@AllArgsConstructor
public enum BusinessType {

    /**
     * 请假
     */
    LEAVE(1, "请假"),

    /**
     * 调课
     */
    COURSE_CHANGE(2, "调课"),

    /**
     * 换课
     */
    COURSE_SWAP(3, "换课"),

    /**
     * 调班
     */
    CLASS_TRANSFER(4, "调班");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 业务类型码
     * @return 枚举对象
     */
    public static BusinessType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BusinessType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
