package com.example.studentservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 更新学生请求
 */
@Data
public class StudentUpdateRequest {

    /**
     * 学生姓名
     */
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
