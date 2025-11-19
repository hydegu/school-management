package com.example.teacherservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.teacherservice.dto.request.TeacherCreateRequest;
import com.example.teacherservice.dto.request.TeacherQueryRequest;
import com.example.teacherservice.dto.request.TeacherUpdateRequest;
import com.example.teacherservice.vo.TeacherVO;

/**
 * 教师服务接口
 */
public interface TeacherService {

    /**
     * 创建教师
     *
     * @param request 创建请求
     * @return 教师ID
     */
    Long createTeacher(TeacherCreateRequest request);

    /**
     * 更新教师信息
     *
     * @param id      教师ID
     * @param request 更新请求
     * @return 是否成功
     */
    Boolean updateTeacher(Long id, TeacherUpdateRequest request);

    /**
     * 删除教师（软删除）
     *
     * @param id 教师ID
     * @return 是否成功
     */
    Boolean deleteTeacher(Long id);

    /**
     * 获取教师详情
     *
     * @param id 教师ID
     * @return 教师VO
     */
    TeacherVO getTeacherDetail(Long id);

    /**
     * 根据教师编号查询教师
     *
     * @param teacherNo 教师编号
     * @return 教师VO
     */
    TeacherVO getTeacherByNo(String teacherNo);

    /**
     * 分页查询教师列表
     *
     * @param request 查询请求
     * @return 教师列表
     */
    IPage<TeacherVO> getTeacherList(TeacherQueryRequest request);
}
