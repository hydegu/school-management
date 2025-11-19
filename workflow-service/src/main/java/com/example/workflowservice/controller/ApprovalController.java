package com.example.workflowservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.utils.R;
import com.example.workflowservice.dto.request.ApprovalQueryRequest;
import com.example.workflowservice.service.ApprovalService;
import com.example.workflowservice.vo.ApprovalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 审批管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/workflow/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    /**
     * 获取审批详情
     *
     * @param id 审批ID
     * @return 审批详情
     */
    @GetMapping("/{id}")
    public R<ApprovalVO> getApprovalDetail(@PathVariable Long id) {
        try {
            ApprovalVO approvalVO = approvalService.getApprovalDetail(id);
            return R.ok(approvalVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询审批详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询审批详情异常: ", e);
            return R.error("查询审批详情失败");
        }
    }

    /**
     * 分页查询审批列表
     *
     * @param request 查询请求
     * @return 审批列表
     */
    @GetMapping("/list")
    public R<IPage<ApprovalVO>> getApprovalList(ApprovalQueryRequest request) {
        try {
            IPage<ApprovalVO> page = approvalService.getApprovalList(request);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询审批列表异常: ", e);
            return R.error("查询审批列表失败");
        }
    }
}
