package com.example.studentservice.dto.request;

import lombok.Data;

/**
 * 学生查询请求
 */
@Data
public class StudentQueryRequest {

    /**
     * 关键词搜索（姓名或学号）
     */
    private String keyword;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 性别：1-男 2-女
     */
    private Integer gender;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
