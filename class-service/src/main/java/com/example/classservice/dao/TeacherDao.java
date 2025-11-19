package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.classservice.entity.EduTeacher;
import org.apache.ibatis.annotations.Mapper;

/**
 * 教师Mapper接口
 */
@Mapper
public interface TeacherDao extends BaseMapper<EduTeacher> {
}
