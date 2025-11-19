package com.example.workflowservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审批VO
 */
@Data
public class ApprovalVO {

    /**
     * 审批记录ID
     */
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
     * 流程名称
     */
    private String processName;

    /**
     * 申请人ID
     */
    private Long applyUserId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

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
     * 业务类型描述
     */
    private String businessTypeDesc;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 审批状态：1-待审批 2-审批中 3-已通过 4-已拒绝 5-已撤回
     */
    private Integer approvalStatus;

    /**
     * 审批状态描述
     */
    private String approvalStatusDesc;

    /**
     * 当前审批人ID
     */
    private Long currentApproverId;

    /**
     * 当前审批人姓名
     */
    private String currentApproverName;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 审批节点列表
     */
    private List<ApprovalNodeVO> nodes;

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
