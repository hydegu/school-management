package com.example.classservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.classservice.entity.EduSchedule;
import com.example.classservice.vo.ScheduleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课表Mapper接口
 */
@Mapper
public interface ScheduleDao extends BaseMapper<EduSchedule> {

    /**
     * 查询班级课表（带关联信息）
     *
     * @param classId  班级ID
     * @param semester 学期
     * @return 课表列表
     */
    List<ScheduleVO> selectSchedulesByClassId(
            @Param("classId") Long classId,
            @Param("semester") String semester
    );

    /**
     * 查询教师课表（带关联信息）
     *
     * @param teacherId 教师ID
     * @param semester  学期
     * @return 课表列表
     */
    List<ScheduleVO> selectSchedulesByTeacherId(
            @Param("teacherId") Long teacherId,
            @Param("semester") String semester
    );

    /**
     * 检查时间冲突（教师）
     *
     * @param teacherId 教师ID
     * @param weekDay   星期几
     * @param period    第几节课
     * @param semester  学期
     * @param excludeId 排除的课表ID（用于更新时排除自己）
     * @return 冲突数量
     */
    int checkTeacherConflict(
            @Param("teacherId") Long teacherId,
            @Param("weekDay") Integer weekDay,
            @Param("period") Integer period,
            @Param("semester") String semester,
            @Param("excludeId") Long excludeId
    );

    /**
     * 检查时间冲突（教室）
     *
     * @param classroom 教室
     * @param weekDay   星期几
     * @param period    第几节课
     * @param semester  学期
     * @param excludeId 排除的课表ID
     * @return 冲突数量
     */
    int checkClassroomConflict(
            @Param("classroom") String classroom,
            @Param("weekDay") Integer weekDay,
            @Param("period") Integer period,
            @Param("semester") String semester,
            @Param("excludeId") Long excludeId
    );
}
