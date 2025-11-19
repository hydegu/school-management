package com.example.classservice.vo;

import lombok.Data;

import java.time.LocalTime;

/**
 * 课表视图对象
 */
@Data
public class ScheduleVO {

    /**
     * 课表ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 星期几：1-7
     */
    private Integer weekDay;

    /**
     * 星期几描述
     */
    private String weekDayDesc;

    /**
     * 第几节课
     */
    private Integer period;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 学期
     */
    private String semester;

    /**
     * 状态
     */
    private Integer status;
}
