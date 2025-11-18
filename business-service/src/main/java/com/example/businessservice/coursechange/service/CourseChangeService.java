package com.example.businessservice.coursechange.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.businessservice.coursechange.dto.CourseChangeApplyRequest;
import com.example.businessservice.coursechange.dto.CourseChangeApproveRequest;
import com.example.businessservice.coursechange.dto.CourseChangeQueryRequest;
import com.example.businessservice.coursechange.vo.CourseChangeVO;

/**
 * 调课申请服务接口
 */
public interface CourseChangeService {

    /**
     * 提交调课申请
     *
     * @param request 调课申请请求
     * @return 调课ID
     */
    Long applyCourseChange(CourseChangeApplyRequest request);

    /**
     * 查询调课详情
     *
     * @param id 调课ID
     * @return 调课详情
     */
    CourseChangeVO getCourseChangeDetail(Long id);

    /**
     * 分页查询调课列表
     *
     * @param request 查询请求
     * @return 调课列表
     */
    IPage<CourseChangeVO> getCourseChangeList(CourseChangeQueryRequest request);

    /**
     * 撤回调课申请
     *
     * @param id        调课ID
     * @param teacherId 教师ID（校验权限）
     * @return 是否成功
     */
    Boolean cancelCourseChange(Long id, Long teacherId);

    /**
     * 审批调课申请
     *
     * @param request 审批请求
     * @return 是否成功
     */
    Boolean approveCourseChange(CourseChangeApproveRequest request);

    /**
     * 查询我的调课记录
     *
     * @param teacherId 教师ID
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @return 调课列表
     */
    IPage<CourseChangeVO> getMyCourseChangeList(Long teacherId, Integer pageNum, Integer pageSize);
}
