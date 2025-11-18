package com.example.businessservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.businessservice.dto.LeaveApplyRequest;
import com.example.businessservice.dto.LeaveApproveRequest;
import com.example.businessservice.dto.LeaveQueryRequest;
import com.example.businessservice.vo.LeaveVO;

/**
 * 请假申请服务接口
 */
public interface LeaveService {

    /**
     * 提交请假申请
     *
     * @param request 请假申请请求
     * @return 请假ID
     */
    Long applyLeave(LeaveApplyRequest request);

    /**
     * 查询请假详情
     *
     * @param id 请假ID
     * @return 请假详情
     */
    LeaveVO getLeaveDetail(Long id);

    /**
     * 分页查询请假列表
     *
     * @param request 查询请求
     * @return 请假列表
     */
    IPage<LeaveVO> getLeaveList(LeaveQueryRequest request);

    /**
     * 撤回请假申请
     *
     * @param id        请假ID
     * @param studentId 学生ID（校验权限）
     * @return 是否成功
     */
    Boolean cancelLeave(Long id, Long studentId);

    /**
     * 审批请假申请
     *
     * @param request 审批请求
     * @return 是否成功
     */
    Boolean approveLeave(LeaveApproveRequest request);

    /**
     * 查询我的请假记录
     *
     * @param studentId 学生ID
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 请假列表
     */
    IPage<LeaveVO> getMyLeaveList(Long studentId, Integer pageNum, Integer pageSize);
}
