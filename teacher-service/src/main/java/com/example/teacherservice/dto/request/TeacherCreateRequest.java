package com.example.teacherservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 创建教师请求
 */
@Data
public class TeacherCreateRequest {

    /**
     * 关联用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 教师编号
     */
    @NotBlank(message = "教师编号不能为空")
    private String teacherNo;

    /**
     * 教师姓名
     */
    @NotBlank(message = "教师姓名不能为空")
    private String teacherName;

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
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 职称（如：班主任/语文老师）
     */
    private String title;

    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate hireDate;
}
