package com.example.businessservice.leave.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假申请实体类
 */
@Data
@TableName("biz_leave")
@Accessors(chain = true)
public class BizLeave extends BaseEntity {

    /**
     * 请假ID
     */
    @TableId(type = IdType.AUTO)
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
     * 请假类型：1-病假 2-事假 3-其他
     */
    private Integer leaveType;

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
     * 证明材料（JSON数组）
     */
    private String proofFiles;

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
     * 关联审批记录ID
     */
    private Long approvalId;

    /**
     * 备注
     */
    private String remark;
}
