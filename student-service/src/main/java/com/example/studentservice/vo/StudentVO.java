package com.example.studentservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生VO
 */
@Data
public class StudentVO {

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 性别：1-男 2-女
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
     * 身份证号
     */
    private String idCard;

    /**
     * 当前班级ID
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 当前年级ID
     */
    private Long gradeId;

    /**
     * 年级名称
     */
    private String gradeName;

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
