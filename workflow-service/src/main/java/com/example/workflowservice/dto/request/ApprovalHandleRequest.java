package com.example.workflowservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 审批处理请求
 */
@Data
public class ApprovalHandleRequest {

    /**
     * 审批意见
     */
    @NotBlank(message = "审批意见不能为空")
    private String opinion;
}
