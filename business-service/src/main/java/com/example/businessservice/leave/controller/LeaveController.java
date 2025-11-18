package com.example.businessservice.leave.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.businessservice.leave.dto.LeaveApplyRequest;
import com.example.businessservice.leave.dto.LeaveApproveRequest;
import com.example.businessservice.leave.dto.LeaveQueryRequest;
import com.example.businessservice.leave.service.LeaveService;
import com.example.businessservice.leave.vo.LeaveVO;
import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 请假申请控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/business/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    /**
     * 提交请假申请
     *
     * @param request 请假申请请求
     * @return 请假ID
     */
    @PostMapping
    public R<Long> applyLeave(@RequestBody LeaveApplyRequest request) {
        try {
            Long leaveId = leaveService.applyLeave(request);
            return R.ok("请假申请提交成功", leaveId);
        } catch (IllegalArgumentException e) {
            log.warn("请假申请失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("请假申请异常: ", e);
            return R.error("请假申请失败");
        }
    }

    /**
     * 查询请假详情
     *
     * @param id 请假ID
     * @return 请假详情
     */
    @GetMapping("/{id}")
    public R<LeaveVO> getLeaveDetail(@PathVariable Long id) {
        try {
            LeaveVO leaveVO = leaveService.getLeaveDetail(id);
            return R.ok(leaveVO);
        } catch (IllegalArgumentException e) {
            log.warn("查询请假详情失败: {}", e.getMessage());
            return R.notFound(e.getMessage());
        } catch (Exception e) {
            log.error("查询请假详情异常: ", e);
            return R.error("查询请假详情失败");
        }
    }

    /**
     * 分页查询请假列表
     *
     * @param request 查询请求
     * @return 请假列表
     */
    @GetMapping
    public R<IPage<LeaveVO>> getLeaveList(LeaveQueryRequest request) {
        try {
            IPage<LeaveVO> page = leaveService.getLeaveList(request);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询请假列表异常: ", e);
            return R.error("查询请假列表失败");
        }
    }

    /**
     * 查询我的请假记录
     *
     * @param studentId 学生ID
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 请假列表
     */
    @GetMapping("/my")
    public R<IPage<LeaveVO>> getMyLeaveList(
            @RequestParam Long studentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            IPage<LeaveVO> page = leaveService.getMyLeaveList(studentId, pageNum, pageSize);
            return R.ok(page);
        } catch (Exception e) {
            log.error("查询我的请假记录异常: ", e);
            return R.error("查询我的请假记录失败");
        }
    }

    /**
     * 撤回请假申请
     *
     * @param id        请假ID
     * @param studentId 学生ID
     * @return 是否成功
     */
    @PutMapping("/{id}/cancel")
    public R<Boolean> cancelLeave(@PathVariable Long id, @RequestParam Long studentId) {
        try {
            Boolean success = leaveService.cancelLeave(id, studentId);
            return success ? R.ok("撤回成功", true) : R.error("撤回失败");
        } catch (IllegalArgumentException e) {
            log.warn("撤回请假失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("撤回请假异常: ", e);
            return R.error("撤回请假失败");
        }
    }

    /**
     * 审批请假申请
     *
     * @param id      请假ID
     * @param request 审批请求
     * @return 是否成功
     */
    @PostMapping("/{id}/approve")
    public R<Boolean> approveLeave(@PathVariable Long id, @RequestBody LeaveApproveRequest request) {
        try {
            request.setLeaveId(id);
            Boolean success = leaveService.approveLeave(request);
            return success ? R.ok("审批成功", true) : R.error("审批失败");
        } catch (IllegalArgumentException e) {
            log.warn("审批请假失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("审批请假异常: ", e);
            return R.error("审批请假失败");
        }
    }
}
