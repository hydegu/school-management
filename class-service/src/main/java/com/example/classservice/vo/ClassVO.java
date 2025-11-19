package com.example.classservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级视图对象
 */
@Data
public class ClassVO {

    /**
     * 班级ID
     */
    private Long id;

    /**
     * 班级编号
     */
    private String classNo;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 年级名称
     */
    private String gradeName;

    /**
     * 班主任ID
     */
    private Long headTeacherId;

    /**
     * 班主任姓名
     */
    private String headTeacherName;

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
     * 学年
     */
    private String schoolYear;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}
