package com.example.businessservice.coursechange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 调课申请请求DTO
 */
@Data
public class CourseChangeApplyRequest implements Serializable {

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
     * 原课程表ID
     */
    private Long originalScheduleId;

    /**
     * 原上课日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate originalDate;

    /**
     * 原上课节次
     */
    private Integer originalPeriod;

    /**
     * 新上课日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate newDate;

    /**
     * 新上课节次
     */
    private Integer newPeriod;

    /**
     * 新教室
     */
    private String newClassroom;

    /**
     * 调课原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;
}
