package com.example.businessservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 请假审批请求DTO
 */
@Data
public class LeaveApproveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假ID
     */
    private Long leaveId;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 审批结果：2-通过 3-拒绝
     */
    private Integer approvalResult;

    /**
     * 审批意见
     */
    private String approvalOpinion;
}
