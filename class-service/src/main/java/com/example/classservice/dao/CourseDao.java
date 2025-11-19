package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.classservice.entity.EduCourse;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseDao extends BaseMapper<EduCourse> {
}
