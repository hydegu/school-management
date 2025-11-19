package com.example.workflowservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.workflowservice.entity.FlowApprovalNode;
import com.example.workflowservice.vo.ApprovalNodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批节点Dao
 */
public interface FlowApprovalNodeDao extends BaseMapper<FlowApprovalNode> {

    /**
     * 根据审批ID查询节点列表
     *
     * @param approvalId 审批ID
     * @return 节点列表
     */
    List<ApprovalNodeVO> selectNodesByApprovalId(@Param("approvalId") Long approvalId);
}
