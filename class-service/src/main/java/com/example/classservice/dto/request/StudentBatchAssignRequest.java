package com.example.classservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

/**
 * 批量分配学生请求DTO
 */
@Data
public class StudentBatchAssignRequest {

    /**
     * 班级ID
     */
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    /**
     * 学生ID列表
     */
    @NotEmpty(message = "学生ID列表不能为空")
    private List<Long> studentIds;
}
