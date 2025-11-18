package com.example.businessservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 换课申请请求DTO
 */
@Data
public class CourseSwapApplyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申请教师ID
     */
    private Long applyTeacherId;

    /**
     * 申请教师姓名
     */
    private String applyTeacherName;

    /**
     * 申请人课程表ID
     */
    private Long applyScheduleId;

    /**
     * 目标教师ID
     */
    private Long targetTeacherId;

    /**
     * 目标教师姓名
     */
    private String targetTeacherName;

    /**
     * 目标教师课程表ID
     */
    private Long targetScheduleId;

    /**
     * 换课原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;
}
