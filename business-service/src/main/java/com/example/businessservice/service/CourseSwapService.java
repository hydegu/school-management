package com.example.businessservice.service;

import com.example.businessservice.dto.CourseSwapApplyRequest;
import com.example.businessservice.dto.CourseSwapApproveRequest;
import com.example.businessservice.dto.CourseSwapConfirmRequest;
import com.example.businessservice.vo.CourseSwapVO;

/**
 * 换课申请服务接口
 */
public interface CourseSwapService {

    /**
     * 提交换课申请
     *
     * @param request 换课申请请求
     * @return 换课ID
     */
    Long applyCourseSwap(CourseSwapApplyRequest request);

    /**
     * 查询换课详情
     *
     * @param id 换课ID
     * @return 换课详情
     */
    CourseSwapVO getCourseSwapDetail(Long id);

    /**
     * 撤回换课申请
     *
     * @param id        换课ID
     * @param teacherId 教师ID
     * @return 是否成功
     */
    Boolean cancelCourseSwap(Long id, Long teacherId);

    /**
     * 目标教师确认换课
     *
     * @param request 确认请求
     * @return 是否成功
     */
    Boolean confirmCourseSwap(CourseSwapConfirmRequest request);

    /**
     * 审批换课申请
     *
     * @param request 审批请求
     * @return 是否成功
     */
    Boolean approveCourseSwap(CourseSwapApproveRequest request);
}
