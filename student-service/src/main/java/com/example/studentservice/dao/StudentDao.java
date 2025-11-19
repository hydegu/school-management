package com.example.studentservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.studentservice.dto.request.StudentQueryRequest;
import com.example.studentservice.entity.EduStudent;
import com.example.studentservice.vo.StudentVO;
import org.apache.ibatis.annotations.Param;

/**
 * 学生Dao
 */
public interface StudentDao extends BaseMapper<EduStudent> {

    /**
     * 分页查询学生列表
     *
     * @param page    分页对象
     * @param request 查询请求
     * @return 学生VO列表
     */
    IPage<StudentVO> selectStudentPage(Page<?> page, @Param("req") StudentQueryRequest request);

    /**
     * 根据ID查询学生详情
     *
     * @param id 学生ID
     * @return 学生VO
     */
    StudentVO selectStudentDetailById(@Param("id") Long id);

    /**
     * 根据学号查询学生
     *
     * @param studentNo 学号
     * @return 学生VO
     */
    StudentVO selectStudentByNo(@Param("studentNo") String studentNo);
}
