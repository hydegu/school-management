package com.example.classservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 课程实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_course")
@Accessors(chain = true)
public class EduCourse extends BaseEntity {

    /**
     * 课程ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 科目ID
     */
    private Long subjectId;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 任课教师ID
     */
    private Long teacherId;

    /**
     * 学期（如：2023-2024-1）
     */
    private String semester;

    /**
     * 每周课时数
     */
    private Integer weeklyHours;

    /**
     * 总课时数
     */
    private Integer totalHours;

    /**
     * 状态：0-停用 1-进行中 2-已结束
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
