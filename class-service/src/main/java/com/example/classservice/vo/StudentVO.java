package com.example.classservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 学生视图对象
 */
@Data
public class StudentVO {

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 性别描述
     */
    private String genderDesc;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthDate;

    /**
     * 当前班级ID
     */
    private Long classId;

    /**
     * 当前班级名称
     */
    private String className;

    /**
     * 当前年级ID
     */
    private Long gradeId;

    /**
     * 当前年级名称
     */
    private String gradeName;
}
