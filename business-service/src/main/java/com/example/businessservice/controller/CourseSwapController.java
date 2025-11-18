package com.example.businessservice.controller;

import com.example.businessservice.dto.CourseSwapApplyRequest;
import com.example.businessservice.dto.CourseSwapApproveRequest;
import com.example.businessservice.dto.CourseSwapConfirmRequest;
import com.example.businessservice.service.CourseSwapService;
import com.example.businessservice.vo.CourseSwapVO;
import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 换课申请控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/business/course-swap")
@RequiredArgsConstructor
public class CourseSwapController {

    private final CourseSwapService courseSwapService;

    /**
     * 提交换课申请
     *
     * @param request 换课申请请求
     * @return 换课ID
     */
    @PostMapping
    public R<Long> applyCourseSwap(@RequestBody CourseSwapApplyRequest request) {
        try {
            Long swapId = courseSwapService.applyCourseSwap(request);
            return R.ok("换课申请提交成功", swapId);
        } catch (IllegalArgumentException e) {
            log.warn("换课申请失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("换课申请异常: ", e);
            return R.error("换课申请失败");
        }
    }

    /**
     * 查询换课详情
     *
     * @param id 换课ID
     * @return 换课详情
     */
    @GetMapping("/{id}")
    public R<CourseSwapVO> getCourseSwapDetail(@PathVariable Long id) {
        try {
            CourseSwapVO courseSwapVO = courseSwapService.getCourseSwapDetail(id);
            return R.ok(courseSwapVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询换课详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询换课详情异常: ", e);
            return R.error("查询换课详情失败");
        }
    }

    /**
     * 撤回换课申请
     *
     * @param id        换课ID
     * @param teacherId 教师ID
     * @return 是否成功
     */
    @PutMapping("/{id}/cancel")
    public R<Boolean> cancelCourseSwap(@PathVariable Long id, @RequestParam Long teacherId) {
        try {
            Boolean success = courseSwapService.cancelCourseSwap(id, teacherId);
            return success ? R.ok("撤回成功", true) : R.error("撤回失败");
        } catch (IllegalArgumentException e) {
            log.warn("撤回换课失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("撤回换课异常: ", e);
            return R.error("撤回换课失败");
        }
    }

    /**
     * 目标教师确认换课（特有功能）
     *
     * @param id      换课ID
     * @param request 确认请求
     * @return 是否成功
     */
    @PostMapping("/{id}/confirm")
    public R<Boolean> confirmCourseSwap(@PathVariable Long id, @RequestBody CourseSwapConfirmRequest request) {
        try {
            request.setSwapId(id);
            Boolean success = courseSwapService.confirmCourseSwap(request);
            return success ? R.ok("确认成功", true) : R.error("确认失败");
        } catch (IllegalArgumentException e) {
            log.warn("确认换课失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("确认换课异常: ", e);
            return R.error("确认换课失败");
        }
    }

    /**
     * 审批换课申请
     *
     * @param id      换课ID
     * @param request 审批请求
     * @return 是否成功
     */
    @PostMapping("/{id}/approve")
    public R<Boolean> approveCourseSwap(@PathVariable Long id, @RequestBody CourseSwapApproveRequest request) {
        try {
            request.setSwapId(id);
            Boolean success = courseSwapService.approveCourseSwap(request);
            return success ? R.ok("审批成功", true) : R.error("审批失败");
        } catch (IllegalArgumentException e) {
            log.warn("审批换课失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("审批换课异常: ", e);
            return R.error("审批换课失败");
        }
    }
}
