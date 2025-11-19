package com.example.client;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.dto.*;
import com.example.fallback.BusinessFeignClientFallbackFactory;
import com.example.utils.R;
import com.example.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "business-service",
        path = "/api/business",
        fallbackFactory = BusinessFeignClientFallbackFactory.class
)
public interface BusinessFeignClient {

    // ==================== 请假相关接口 ====================

    /**
     * 提交请假申请
     */
    @PostMapping("/leave")
    R<Long> applyLeave(@RequestBody LeaveApplyRequest request);

    /**
     * 查询请假详情
     */
    @GetMapping("/leave/{id}")
    R<LeaveVO> getLeaveDetail(@PathVariable("id") Long id);

    /**
     * 分页查询请假列表
     */
    @GetMapping("/leave")
    R<IPage<LeaveVO>> getLeaveList(@RequestParam(value = "studentId", required = false) Long studentId,
                                    @RequestParam(value = "status", required = false) Integer status,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    /**
     * 查询我的请假记录
     */
    @GetMapping("/leave/my")
    R<IPage<LeaveVO>> getMyLeaveList(@RequestParam("studentId") Long studentId,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    /**
     * 撤回请假申请
     */
    @PutMapping("/leave/{id}/cancel")
    R<Boolean> cancelLeave(@PathVariable("id") Long id, @RequestParam("studentId") Long studentId);

    /**
     * 审批请假申请
     */
    @PostMapping("/leave/{id}/approve")
    R<Boolean> approveLeave(@PathVariable("id") Long id, @RequestBody LeaveApproveRequest request);

    // ==================== 调课相关接口 ====================

    /**
     * 提交调课申请
     */
    @PostMapping("/course-change")
    R<Long> applyCourseChange(@RequestBody CourseChangeApplyRequest request);

    /**
     * 查询调课详情
     */
    @GetMapping("/course-change/{id}")
    R<CourseChangeVO> getCourseChangeDetail(@PathVariable("id") Long id);

    /**
     * 分页查询调课列表
     */
    @GetMapping("/course-change")
    R<IPage<CourseChangeVO>> getCourseChangeList(@RequestParam(value = "teacherId", required = false) Long teacherId,
                                                  @RequestParam(value = "status", required = false) Integer status,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    /**
     * 查询我的调课记录
     */
    @GetMapping("/course-change/my")
    R<IPage<CourseChangeVO>> getMyCourseChangeList(@RequestParam("teacherId") Long teacherId,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

    /**
     * 撤回调课申请
     */
    @PutMapping("/course-change/{id}/cancel")
    R<Boolean> cancelCourseChange(@PathVariable("id") Long id, @RequestParam("teacherId") Long teacherId);

    /**
     * 审批调课申请
     */
    @PostMapping("/course-change/{id}/approve")
    R<Boolean> approveCourseChange(@PathVariable("id") Long id, @RequestBody CourseChangeApproveRequest request);

    // ==================== 换课相关接口 ====================

    /**
     * 提交换课申请
     */
    @PostMapping("/course-swap")
    R<Long> applyCourseSwap(@RequestBody CourseSwapApplyRequest request);

    /**
     * 查询换课详情
     */
    @GetMapping("/course-swap/{id}")
    R<CourseSwapVO> getCourseSwapDetail(@PathVariable("id") Long id);

    /**
     * 撤回换课申请
     */
    @PutMapping("/course-swap/{id}/cancel")
    R<Boolean> cancelCourseSwap(@PathVariable("id") Long id, @RequestParam("teacherId") Long teacherId);

    /**
     * 目标教师确认换课
     */
    @PostMapping("/course-swap/{id}/confirm")
    R<Boolean> confirmCourseSwap(@PathVariable("id") Long id, @RequestBody CourseSwapConfirmRequest request);

    /**
     * 审批换课申请
     */
    @PostMapping("/course-swap/{id}/approve")
    R<Boolean> approveCourseSwap(@PathVariable("id") Long id, @RequestBody CourseSwapApproveRequest request);

    // ==================== 调班相关接口 ====================

    /**
     * 提交调班申请
     */
    @PostMapping("/class-transfer")
    R<Long> applyClassTransfer(@RequestBody ClassTransferApplyRequest request);

    /**
     * 健康检查
     */
    @GetMapping("/class-transfer/health")
    R<String> health();
}
