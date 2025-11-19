package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.classservice.entity.EduGrade;
import org.apache.ibatis.annotations.Mapper;

/**
 * 年级Mapper接口
 */
@Mapper
public interface GradeDao extends BaseMapper<EduGrade> {
}
