package com.example.classservice.dto.request;

import lombok.Data;

/**
 * 更新班级请求DTO
 */
@Data
public class ClassUpdateRequest {

    /**
     * 班级名称
     */
    private String className;

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
    private Integer maxStudents;

    /**
     * 状态：0-停用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
