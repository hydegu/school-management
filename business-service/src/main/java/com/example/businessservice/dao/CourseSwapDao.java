package com.example.businessservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.businessservice.entity.BizCourseSwap;
import org.apache.ibatis.annotations.Mapper;

/**
 * 换课申请Dao
 */
@Mapper
public interface CourseSwapDao extends BaseMapper<BizCourseSwap> {
}
