package com.example.businessservice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.businessservice.dto.CourseChangeApplyRequest;
import com.example.businessservice.dto.CourseChangeApproveRequest;
import com.example.businessservice.dto.CourseChangeQueryRequest;
import com.example.businessservice.service.CourseChangeService;
import com.example.businessservice.vo.CourseChangeVO;
import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 调课申请控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/business/course-change")
@RequiredArgsConstructor
public class CourseChangeController {

    private final CourseChangeService courseChangeService;

    /**
     * 提交调课申请
     *
     * @param request 调课申请请求
     * @return 调课ID
     */
    @PostMapping
    public R<Long> applyCourseChange(@RequestBody CourseChangeApplyRequest request) {
        try {
            Long changeId = courseChangeService.applyCourseChange(request);
            return R.ok("调课申请提交成功", changeId);
        } catch (IllegalArgumentException e) {
            log.warn("调课申请失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("调课申请异常: ", e);
            return R.error("调课申请失败");
        }
    }

    /**
     * 查询调课详情
     *
     * @param id 调课ID
     * @return 调课详情
     */
    @GetMapping("/{id}")
    public R<CourseChangeVO> getCourseChangeDetail(@PathVariable Long id) {
        try {
            CourseChangeVO courseChangeVO = courseChangeService.getCourseChangeDetail(id);
            return R.ok(courseChangeVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询调课详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询调课详情异常: ", e);
            return R.error("查询调课详情失败");
        }
    }

    /**
     * 分页查询调课列表
     *
     * @param request 查询请求
     * @return 调课列表
     */
    @GetMapping
    public R<IPage<CourseChangeVO>> getCourseChangeList(CourseChangeQueryRequest request) {
        try {
            IPage<CourseChangeVO> page = courseChangeService.getCourseChangeList(request);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询调课列表异常: ", e);
            return R.error("查询调课列表失败");
        }
    }

    /**
     * 查询我的调课记录
     *
     * @param teacherId 教师ID
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 调课列表
     */
    @GetMapping("/my")
    public R<IPage<CourseChangeVO>> getMyCourseChangeList(
            @RequestParam Long teacherId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            IPage<CourseChangeVO> page = courseChangeService.getMyCourseChangeList(teacherId, pageNum, pageSize);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询我的调课记录异常: ", e);
            return R.error("查询我的调课记录失败");
        }
    }

    /**
     * 撤回调课申请
     *
     * @param id        调课ID
     * @param teacherId 教师ID
     * @return 是否成功
     */
    @PutMapping("/{id}/cancel")
    public R<Boolean> cancelCourseChange(@PathVariable Long id, @RequestParam Long teacherId) {
        try {
            Boolean success = courseChangeService.cancelCourseChange(id, teacherId);
            return success ? R.ok("撤回成功", true) : R.error("撤回失败");
        } catch (IllegalArgumentException e) {
            log.warn("撤回调课失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("撤回调课异常: ", e);
            return R.error("撤回调课失败");
        }
    }

    /**
     * 审批调课申请
     *
     * @param id      调课ID
     * @param request 审批请求
     * @return 是否成功
     */
    @PostMapping("/{id}/approve")
    public R<Boolean> approveCourseChange(@PathVariable Long id, @RequestBody CourseChangeApproveRequest request) {
        try {
            request.setChangeId(id);
            Boolean success = courseChangeService.approveCourseChange(request);
            return success ? R.ok("审批成功", true) : R.error("审批失败");
        } catch (IllegalArgumentException e) {
            log.warn("审批调课失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("审批调课异常: ", e);
            return R.error("审批调课失败");
        }
    }
}
