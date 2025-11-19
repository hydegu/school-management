package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.classservice.entity.EduClass;
import com.example.classservice.vo.ClassVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 班级Mapper接口
 */
@Mapper
public interface ClassDao extends BaseMapper<EduClass> {

    /**
     * 分页查询班级列表（带关联信息）
     *
     * @param page       分页对象
     * @param className  班级名称（模糊查询）
     * @param gradeId    年级ID
     * @param headTeacherId 班主任ID
     * @param schoolYear 学年
     * @param status     状态
     * @return 班级列表
     */
    IPage<ClassVO> selectClassPage(
            Page<ClassVO> page,
            @Param("className") String className,
            @Param("gradeId") Long gradeId,
            @Param("headTeacherId") Long headTeacherId,
            @Param("schoolYear") String schoolYear,
            @Param("status") Integer status
    );

    /**
     * 根据ID查询班级详情（带关联信息）
     *
     * @param id 班级ID
     * @return 班级详情
     */
    ClassVO selectClassDetailById(@Param("id") Long id);

    /**
     * 更新班级学生数
     *
     * @param classId 班级ID
     * @param count   变化数量（正数为增加，负数为减少）
     * @return 更新行数
     */
    int updateStudentCount(@Param("classId") Long classId, @Param("count") Integer count);
}
