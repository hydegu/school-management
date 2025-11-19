package com.example.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 调课审批请求DTO
 */
@Data
public class CourseChangeApproveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调课ID
     */
    private Long changeId;

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
