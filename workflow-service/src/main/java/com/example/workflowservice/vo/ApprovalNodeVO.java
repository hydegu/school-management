package com.example.workflowservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批节点VO
 */
@Data
public class ApprovalNodeVO {

    /**
     * 节点记录ID
     */
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
     * 节点状态描述
     */
    private String approvalStatusDesc;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    private String approvalOpinion;
}
