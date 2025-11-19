package com.example.workflowservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审批节点记录实体类
 */
@Data
@TableName("flow_approval_node")
@Accessors(chain = true)
public class FlowApprovalNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审批记录ID
     */
    private Long approvalId;

    /**
     * 节点层级
     */
    private Integer nodeLevel;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 节点状态：1-待审批 2-已通过 3-已拒绝
     */
    private Integer approvalStatus;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    private String approvalOpinion;

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
