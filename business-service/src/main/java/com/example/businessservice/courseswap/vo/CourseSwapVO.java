package com.example.businessservice.courseswap.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 换课申请视图对象
 */
@Data
public class CourseSwapVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 换课ID
     */
    private Long id;

    /**
     * 换课单号
     */
    private String swapNo;

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
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applyTime;

    /**
     * 对方确认：0-未确认 1-已确认 2-已拒绝
     */
    private Integer targetConfirm;

    /**
     * 对方确认状态描述
     */
    private String targetConfirmDesc;

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
