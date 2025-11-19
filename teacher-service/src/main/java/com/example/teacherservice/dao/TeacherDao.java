package com.example.teacherservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.teacherservice.dto.request.TeacherQueryRequest;
import com.example.teacherservice.entity.EduTeacher;
import com.example.teacherservice.vo.TeacherVO;
import org.apache.ibatis.annotations.Param;

/**
 * 教师Dao
 */
public interface TeacherDao extends BaseMapper<EduTeacher> {

    /**
     * 分页查询教师列表
     *
     * @param page    分页对象
     * @param request 查询请求
     * @return 教师VO列表
     */
    IPage<TeacherVO> selectTeacherPage(Page<?> page, @Param("req") TeacherQueryRequest request);

    /**
     * 根据ID查询教师详情
     *
     * @param id 教师ID
     * @return 教师VO
     */
    TeacherVO selectTeacherDetailById(@Param("id") Long id);

    /**
     * 根据教师编号查询教师
     *
     * @param teacherNo 教师编号
     * @return 教师VO
     */
    TeacherVO selectTeacherByNo(@Param("teacherNo") String teacherNo);
}
