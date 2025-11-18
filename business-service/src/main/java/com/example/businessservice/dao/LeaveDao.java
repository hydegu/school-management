package com.example.businessservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.businessservice.entity.BizLeave;
import com.example.businessservice.vo.LeaveVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * 请假申请Dao
 */
@Mapper
public interface LeaveDao extends BaseMapper<BizLeave> {

    /**
     * 分页查询请假申请列表（带班级名称）
     *
     * @param page          分页对象
     * @param studentId     学生ID
     * @param classId       班级ID
     * @param leaveType     请假类型
     * @param approvalStatus 审批状态
     * @param startDate     开始日期
     * @param endDate       结束日期
     * @return 请假申请分页列表
     */
    @Select("""
        <script>
        SELECT
            l.*,
            c.class_name as className
        FROM biz_leave l
        LEFT JOIN edu_class c ON l.class_id = c.id
        WHERE l.is_deleted = 0
        <if test="studentId != null">
            AND l.student_id = #{studentId}
        </if>
        <if test="classId != null">
            AND l.class_id = #{classId}
        </if>
        <if test="leaveType != null">
            AND l.leave_type = #{leaveType}
        </if>
        <if test="approvalStatus != null">
            AND l.approval_status = #{approvalStatus}
        </if>
        <if test="startDate != null">
            AND l.start_date &gt;= #{startDate}
        </if>
        <if test="endDate != null">
            AND l.end_date &lt;= #{endDate}
        </if>
        ORDER BY l.created_at DESC
        </script>
    """)
    IPage<LeaveVO> selectLeavePage(
            Page<LeaveVO> page,
            @Param("studentId") Long studentId,
            @Param("classId") Long classId,
            @Param("leaveType") Integer leaveType,
            @Param("approvalStatus") Integer approvalStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 根据ID查询请假详情（带班级名称）
     *
     * @param id 请假ID
     * @return 请假详情
     */
    @Select("""
        SELECT
            l.*,
            c.class_name as className
        FROM biz_leave l
        LEFT JOIN edu_class c ON l.class_id = c.id
        WHERE l.id = #{id} AND l.is_deleted = 0
    """)
    LeaveVO selectLeaveDetailById(@Param("id") Long id);
}
