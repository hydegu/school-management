package com.example.workflowservice.dto.request;

import lombok.Data;

/**
 * 审批查询请求
 */
@Data
public class ApprovalQueryRequest {

    /**
     * 审批编号
     */
    private String approvalNo;

    /**
     * 申请人ID
     */
    private Long applyUserId;

    /**
     * 业务类型：1-请假 2-调课 3-换课 4-调班
     */
    private Integer businessType;

    /**
     * 审批状态：1-待审批 2-审批中 3-已通过 4-已拒绝 5-已撤回
     */
    private Integer approvalStatus;

    /**
     * 当前审批人ID
     */
    private Long currentApproverId;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
