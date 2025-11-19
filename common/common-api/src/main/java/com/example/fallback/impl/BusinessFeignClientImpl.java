package com.example.fallback.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.client.BusinessFeignClient;
import com.example.dto.*;
import com.example.utils.R;
import com.example.vo.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BusinessFeignClientImpl implements BusinessFeignClient {
    private final Throwable cause;

    // ==================== 请假相关接口降级 ====================

    @Override
    public R<Long> applyLeave(LeaveApplyRequest request) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<LeaveVO> getLeaveDetail(Long id) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<IPage<LeaveVO>> getLeaveList(Long studentId, Integer status, Integer pageNum, Integer pageSize) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<IPage<LeaveVO>> getMyLeaveList(Long studentId, Integer pageNum, Integer pageSize) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> cancelLeave(Long id, Long studentId) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> approveLeave(Long id, LeaveApproveRequest request) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Long> applyCourseChange(CourseChangeApplyRequest request) {
        return R.error(500, cause.getMessage());
    }

    // ==================== 调课相关接口降级 ====================

    @Override
    public R<CourseChangeVO> getCourseChangeDetail(Long id) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<IPage<CourseChangeVO>> getCourseChangeList(Long teacherId, Integer status, Integer pageNum, Integer pageSize) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<IPage<CourseChangeVO>> getMyCourseChangeList(Long teacherId, Integer pageNum, Integer pageSize) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> cancelCourseChange(Long id, Long teacherId) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> approveCourseChange(Long id, CourseChangeApproveRequest request) {
        return R.error(500, cause.getMessage());
    }

    // ==================== 换课相关接口降级 ====================

    @Override
    public R<Long> applyCourseSwap(CourseSwapApplyRequest request) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<CourseSwapVO> getCourseSwapDetail(Long id) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> cancelCourseSwap(Long id, Long teacherId) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> confirmCourseSwap(Long id, CourseSwapConfirmRequest request) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<Boolean> approveCourseSwap(Long id, CourseSwapApproveRequest request) {
        return R.error(500, cause.getMessage());
    }

    // ==================== 调班相关接口降级 ====================

    @Override
    public R<Long> applyClassTransfer(ClassTransferApplyRequest request) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<String> health() {
        return R.error(500, cause.getMessage());
    }
}
