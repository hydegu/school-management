package com.example.workflowservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.workflowservice.dto.request.ApprovalQueryRequest;
import com.example.workflowservice.entity.FlowApproval;
import com.example.workflowservice.vo.ApprovalVO;
import org.apache.ibatis.annotations.Param;

/**
 * 审批Dao
 */
public interface FlowApprovalDao extends BaseMapper<FlowApproval> {

    /**
     * 分页查询审批列表
     *
     * @param page    分页对象
     * @param request 查询请求
     * @return 审批VO列表
     */
    IPage<ApprovalVO> selectApprovalPage(Page<?> page, @Param("req") ApprovalQueryRequest request);

    /**
     * 根据ID查询审批详情
     *
     * @param id 审批ID
     * @return 审批VO
     */
    ApprovalVO selectApprovalDetailById(@Param("id") Long id);
}
