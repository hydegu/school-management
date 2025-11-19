package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.classservice.entity.EduSubject;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科目Mapper接口
 */
@Mapper
public interface SubjectDao extends BaseMapper<EduSubject> {
}
