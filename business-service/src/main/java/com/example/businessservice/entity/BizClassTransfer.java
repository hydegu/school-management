package com.example.businessservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 调班申请实体类
 */
@Data
@TableName("biz_class_transfer")
@Accessors(chain = true)
public class BizClassTransfer extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调班单号
     */
    private String transferNo;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 原班级ID
     */
    private Long originalClassId;

    /**
     * 原班级名称
     */
    private String originalClassName;

    /**
     * 目标班级ID
     */
    private Long targetClassId;

    /**
     * 目标班级名称
     */
    private String targetClassName;

    /**
     * 调班原因
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
     * 关联审批记录ID
     */
    private Long approvalId;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate effectiveDate;

    /**
     * 备注
     */
    private String remark;
}
