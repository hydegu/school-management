package com.example.classservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.classservice.dto.request.ClassCreateRequest;
import com.example.classservice.dto.request.ClassQueryRequest;
import com.example.classservice.dto.request.ClassUpdateRequest;
import com.example.classservice.dto.request.StudentBatchAssignRequest;
import com.example.classservice.vo.ClassVO;
import com.example.classservice.vo.StudentVO;

/**
 * 班级服务接口
 */
public interface ClassService {

    /**
     * 创建班级
     *
     * @param request 创建请求
     * @return 班级ID
     */
    Long createClass(ClassCreateRequest request);

    /**
     * 更新班级
     *
     * @param id      班级ID
     * @param request 更新请求
     * @return 是否成功
     */
    Boolean updateClass(Long id, ClassUpdateRequest request);

    /**
     * 删除班级（软删除）
     *
     * @param id 班级ID
     * @return 是否成功
     */
    Boolean deleteClass(Long id);

    /**
     * 获取班级详情
     *
     * @param id 班级ID
     * @return 班级详情
     */
    ClassVO getClassDetail(Long id);

    /**
     * 分页查询班级列表
     *
     * @param request 查询请求
     * @return 班级列表
     */
    IPage<ClassVO> getClassList(ClassQueryRequest request);

    /**
     * 分配班主任
     *
     * @param classId       班级ID
     * @param headTeacherId 班主任ID
     * @return 是否成功
     */
    Boolean assignHeadTeacher(Long classId, Long headTeacherId);

    /**
     * 批量分配学生
     *
     * @param request 批量分配请求
     * @return 是否成功
     */
    Boolean batchAssignStudents(StudentBatchAssignRequest request);

    /**
     * 移除学生
     *
     * @param classId   班级ID
     * @param studentId 学生ID
     * @return 是否成功
     */
    Boolean removeStudent(Long classId, Long studentId);

    /**
     * 获取班级学生列表
     *
     * @param classId  班级ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 学生列表
     */
    IPage<StudentVO> getClassStudents(Long classId, Integer pageNum, Integer pageSize);
}
