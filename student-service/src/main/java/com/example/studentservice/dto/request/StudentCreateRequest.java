package com.example.studentservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建学生请求
 */
@Data
public class StudentCreateRequest {

    /**
     * 关联用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 学号
     */
    @NotBlank(message = "学号不能为空")
    private String studentNo;

    /**
     * 学生姓名
     */
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    /**
     * 性别：1-男 2-女
     */
    private Integer gender;

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
     * 当前年级ID
     */
    private Long gradeId;
}
