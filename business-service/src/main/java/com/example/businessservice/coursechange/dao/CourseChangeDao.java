package com.example.businessservice.coursechange.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.businessservice.coursechange.entity.BizCourseChange;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调课申请Dao
 */
@Mapper
public interface CourseChangeDao extends BaseMapper<BizCourseChange> {
}
