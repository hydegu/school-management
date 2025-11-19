package com.example.workflowservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建审批请求
 */
@Data
public class ApprovalCreateRequest {

    /**
     * 流程ID
     */
    @NotNull(message = "流程ID不能为空")
    private Long processId;

    /**
     * 业务类型：1-请假 2-调课 3-换课 4-调班
     */
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;

    /**
     * 关联业务ID
     */
    @NotNull(message = "业务ID不能为空")
    private Long businessId;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 审批人ID列表（按顺序）
     */
    private java.util.List<Long> approverIds;
}
