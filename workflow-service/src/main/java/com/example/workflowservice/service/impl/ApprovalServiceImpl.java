package com.example.workflowservice.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.exception.ResourceNotFoundException;
import com.example.workflowservice.dao.FlowApprovalDao;
import com.example.workflowservice.dao.FlowApprovalNodeDao;
import com.example.workflowservice.dto.request.ApprovalQueryRequest;
import com.example.workflowservice.enums.ApprovalStatus;
import com.example.workflowservice.service.ApprovalService;
import com.example.workflowservice.vo.ApprovalNodeVO;
import com.example.workflowservice.vo.ApprovalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final FlowApprovalDao flowApprovalDao;
    private final FlowApprovalNodeDao flowApprovalNodeDao;

    @Override
    public ApprovalVO getApprovalDetail(Long id) {
        log.info("获取审批详情，审批ID：{}", id);

        ApprovalVO approvalVO = flowApprovalDao.selectApprovalDetailById(id);
        if (approvalVO == null) {
            throw new ResourceNotFoundException("审批记录", id);
        }

        // 设置业务类型描述
        approvalVO.setBusinessTypeDesc(getBusinessTypeDesc(approvalVO.getBusinessType()));

        // 设置审批状态描述
        ApprovalStatus status = ApprovalStatus.fromCode(approvalVO.getApprovalStatus());
        if (status != null) {
            approvalVO.setApprovalStatusDesc(status.getDesc());
        }

        // 查询审批节点
        List<ApprovalNodeVO> nodes = flowApprovalNodeDao.selectNodesByApprovalId(id);
        nodes.forEach(node -> {
            ApprovalStatus nodeStatus = ApprovalStatus.fromCode(node.getApprovalStatus());
            if (nodeStatus != null) {
                node.setApprovalStatusDesc(nodeStatus.getDesc());
            }
        });
        approvalVO.setNodes(nodes);

        return approvalVO;
    }

    @Override
    public IPage<ApprovalVO> getApprovalList(ApprovalQueryRequest request) {
        log.info("分页查询审批列表，请求参数：{}", request);

        // 创建分页对象
        Page<ApprovalVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 查询
        IPage<ApprovalVO> result = flowApprovalDao.selectApprovalPage(page, request);

        // 设置描述信息
        result.getRecords().forEach(approvalVO -> {
            // 业务类型描述
            approvalVO.setBusinessTypeDesc(getBusinessTypeDesc(approvalVO.getBusinessType()));

            // 审批状态描述
            ApprovalStatus status = ApprovalStatus.fromCode(approvalVO.getApprovalStatus());
            if (status != null) {
                approvalVO.setApprovalStatusDesc(status.getDesc());
            }
        });

        return result;
    }

    /**
     * 获取业务类型描述
     */
    private String getBusinessTypeDesc(Integer businessType) {
        if (businessType == null) {
            return null;
        }
        switch (businessType) {
            case 1:
                return "请假";
            case 2:
                return "调课";
            case 3:
                return "换课";
            case 4:
                return "调班";
            default:
                return "未知";
        }
    }
}
