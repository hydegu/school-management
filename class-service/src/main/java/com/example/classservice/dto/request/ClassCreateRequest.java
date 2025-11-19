package com.example.classservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



/**
 * 创建班级请求DTO
 */
@Data
public class ClassCreateRequest {

    /**
     * 班级编号
     */
    @NotBlank(message = "班级编号不能为空")
    private String classNo;

    /**
     * 班级名称
     */
    @NotBlank(message = "班级名称不能为空")
    private String className;

    /**
     * 年级ID
     */
    @NotNull(message = "年级ID不能为空")
    private Long gradeId;

    /**
     * 班主任ID
     */
    private Long headTeacherId;

    /**
     * 上课教室
     */
    private String classroom;

    /**
     * 最大学生数
     */
    private Integer maxStudents = 50;

    /**
     * 学年
     */
    @NotBlank(message = "学年不能为空")
    private String schoolYear;

    /**
     * 备注
     */
    private String remark;
}
