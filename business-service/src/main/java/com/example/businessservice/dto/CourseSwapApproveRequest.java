package com.example.businessservice.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 换课审批请求DTO
 */
@Data
public class CourseSwapApproveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 换课ID
     */
    private Long swapId;

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
