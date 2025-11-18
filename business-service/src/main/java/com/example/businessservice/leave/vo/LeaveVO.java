package com.example.businessservice.leave.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 请假申请视图对象
 */
@Data
public class LeaveVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假ID
     */
    private Long id;

    /**
     * 请假单号
     */
    private String leaveNo;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 请假类型：1-病假 2-事假 3-其他
     */
    private Integer leaveType;

    /**
     * 请假类型描述
     */
    private String leaveTypeDesc;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    /**
     * 请假天数
     */
    private BigDecimal leaveDays;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 证明材料URL列表
     */
    private List<String> proofFiles;

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
