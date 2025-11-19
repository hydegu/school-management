package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.classservice.entity.EduStudent;
import com.example.classservice.vo.StudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 学生Mapper接口
 */
@Mapper
public interface StudentDao extends BaseMapper<EduStudent> {

    /**
     * 查询班级学生列表（带关联信息）
     *
     * @param page    分页对象
     * @param classId 班级ID
     * @return 学生列表
     */
    IPage<StudentVO> selectStudentsByClassId(
            Page<StudentVO> page,
            @Param("classId") Long classId
    );
}
