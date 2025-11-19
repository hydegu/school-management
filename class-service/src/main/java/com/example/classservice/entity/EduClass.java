package com.example.classservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 班级实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_class")
@Accessors(chain = true)
public class EduClass extends BaseEntity {

    /**
     * 班级ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 班级编号
     */
    private String classNo;

    /**
     * 班级名称（如：一年一班）
     */
    private String className;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 班主任ID（教师ID）
     */
    private Long headTeacherId;

    /**
     * 上课教室
     */
    private String classroom;

    /**
     * 最大学生数
     */
    private Integer maxStudents;

    /**
     * 当前学生数
     */
    private Integer currentStudents;

    /**
     * 学年（如：2023-2024）
     */
    private String schoolYear;

    /**
     * 状态：0-停用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
