package com.example.studentservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.studentservice.dto.request.StudentCreateRequest;
import com.example.studentservice.dto.request.StudentQueryRequest;
import com.example.studentservice.dto.request.StudentUpdateRequest;
import com.example.studentservice.vo.StudentVO;

/**
 * 学生服务接口
 */
public interface StudentService {

    /**
     * 创建学生
     *
     * @param request 创建请求
     * @return 学生ID
     */
    Long createStudent(StudentCreateRequest request);

    /**
     * 更新学生信息
     *
     * @param id      学生ID
     * @param request 更新请求
     * @return 是否成功
     */
    Boolean updateStudent(Long id, StudentUpdateRequest request);

    /**
     * 删除学生（软删除）
     *
     * @param id 学生ID
     * @return 是否成功
     */
    Boolean deleteStudent(Long id);

    /**
     * 获取学生详情
     *
     * @param id 学生ID
     * @return 学生VO
     */
    StudentVO getStudentDetail(Long id);

    /**
     * 根据学号查询学生
     *
     * @param studentNo 学号
     * @return 学生VO
     */
    StudentVO getStudentByNo(String studentNo);

    /**
     * 分页查询学生列表
     *
     * @param request 查询请求
     * @return 学生列表
     */
    IPage<StudentVO> getStudentList(StudentQueryRequest request);
}
