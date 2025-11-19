package com.example.workflowservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 审批记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_approval")
@Accessors(chain = true)
public class FlowApproval extends BaseEntity {

    /**
     * 审批记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审批编号
     */
    private String approvalNo;

    /**
     * 流程ID
     */
    private Long processId;

    /**
     * 申请人ID
     */
    private Long applyUserId;

    /**
     * 申请人类型：1-管理员 2-教师 3-学生 4-家长
     */
    private Integer applyUserType;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applyTime;

    /**
     * 业务类型：1-请假 2-调课 3-换课 4-调班
     */
    private Integer businessType;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 审批状态：1-待审批 2-审批中 3-已通过 4-已拒绝 5-已撤回
     */
    private Integer approvalStatus;

    /**
     * 当前审批人ID
     */
    private Long currentApproverId;

    /**
     * 申请原因
     */
    private String reason;
}
