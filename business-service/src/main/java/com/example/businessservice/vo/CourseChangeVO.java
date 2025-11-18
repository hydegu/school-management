package com.example.businessservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 调课申请视图对象
 */
@Data
public class CourseChangeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调课ID
     */
    private Long id;

    /**
     * 调课单号
     */
    private String changeNo;

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
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applyTime;

    /**
     * 审批状态：1-待审批 2-已通过 3-已拒绝 4-已撤回
     */
    private Integer approvalStatus;

    /**
     * 审批状态描述
     */
    private String approvalStatusDesc;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}
