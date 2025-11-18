package com.example.businessservice.courseswap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.businessservice.courseswap.entity.BizCourseSwap;
import org.apache.ibatis.annotations.Mapper;

/**
 * 换课申请Dao
 */
@Mapper
public interface CourseSwapDao extends BaseMapper<BizCourseSwap> {
}
