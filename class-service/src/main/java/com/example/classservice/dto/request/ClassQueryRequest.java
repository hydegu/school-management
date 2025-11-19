package com.example.classservice.dto.request;

import lombok.Data;

/**
 * 班级查询请求DTO
 */
@Data
public class ClassQueryRequest {

    /**
     * 班级名称（模糊查询）
     */
    private String className;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 班主任ID
     */
    private Long headTeacherId;

    /**
     * 学年
     */
    private String schoolYear;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
