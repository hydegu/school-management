package com.example.businessservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请假类型枚举
 */
@Getter
@AllArgsConstructor
public enum LeaveType {

    /**
     * 病假
     */
    SICK(1, "病假"),

    /**
     * 事假
     */
    PERSONAL(2, "事假"),

    /**
     * 其他
     */
    OTHER(3, "其他");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     *
     * @param code 类型码
     * @return 枚举对象
     */
    public static LeaveType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LeaveType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
