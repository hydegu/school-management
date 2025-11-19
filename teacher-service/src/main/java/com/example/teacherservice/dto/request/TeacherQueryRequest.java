package com.example.teacherservice.dto.request;

import lombok.Data;

/**
 * 教师查询请求
 */
@Data
public class TeacherQueryRequest {

    /**
     * 关键词搜索（姓名或教师编号）
     */
    private String keyword;

    /**
     * 性别：1-男 2-女
     */
    private Integer gender;

    /**
     * 职称
     */
    private String title;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
