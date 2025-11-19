package com.example.workflowservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.workflowservice.dto.request.ApprovalQueryRequest;
import com.example.workflowservice.vo.ApprovalVO;

/**
 * 审批服务接口
 */
public interface ApprovalService {

    /**
     * 获取审批详情
     *
     * @param id 审批ID
     * @return 审批VO
     */
    ApprovalVO getApprovalDetail(Long id);

    /**
     * 分页查询审批列表
     *
     * @param request 查询请求
     * @return 审批列表
     */
    IPage<ApprovalVO> getApprovalList(ApprovalQueryRequest request);
}
