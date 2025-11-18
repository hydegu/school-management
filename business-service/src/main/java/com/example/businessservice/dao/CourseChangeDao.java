package com.example.businessservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.businessservice.entity.BizCourseChange;
import com.example.businessservice.vo.CourseChangeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * 调课申请Dao
 */
@Mapper
public interface CourseChangeDao extends BaseMapper<BizCourseChange> {

    /**
     * 分页查询调课申请列表
     */
    @Select("<script>" +
            "SELECT cc.* FROM biz_course_change cc " +
            "WHERE cc.is_deleted = 0 " +
            "<if test='applyTeacherId != null'> AND cc.apply_teacher_id = #{applyTeacherId} </if>" +
            "<if test='approvalStatus != null'> AND cc.approval_status = #{approvalStatus} </if>" +
            "<if test='startDate != null'> AND cc.original_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND cc.original_date &lt;= #{endDate} </if>" +
            "ORDER BY cc.created_at DESC " +
            "</script>")
    IPage<CourseChangeVO> selectCourseChangePage(
            Page<CourseChangeVO> page,
            @Param("applyTeacherId") Long applyTeacherId,
            @Param("approvalStatus") Integer approvalStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 根据ID查询调课详情
     */
    @Select("SELECT cc.* FROM biz_course_change cc WHERE cc.id = #{id} AND cc.is_deleted = 0")
    CourseChangeVO selectCourseChangeDetailById(@Param("id") Long id);
}
