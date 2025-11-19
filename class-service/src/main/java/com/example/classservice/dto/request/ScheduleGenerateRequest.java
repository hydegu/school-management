package com.example.classservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 课表生成请求DTO
 */
@Data
public class ScheduleGenerateRequest {

    /**
     * 班级ID
     */
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    /**
     * 学期（如：2023-2024-1）
     */
    @NotBlank(message = "学期不能为空")
    private String semester;

    /**
     * 课程列表（课程ID和每周课时数）
     */
    @NotNull(message = "课程列表不能为空")
    private List<CourseScheduleInfo> courses;

    /**
     * 课程排课信息
     */
    @Data
    public static class CourseScheduleInfo {
        /**
         * 课程ID
         */
        private Long courseId;

        /**
         * 每周课时数
         */
        private Integer weeklyHours;
    }
}
