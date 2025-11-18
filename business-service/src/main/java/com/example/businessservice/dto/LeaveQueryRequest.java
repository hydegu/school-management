package com.example.businessservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 请假查询请求DTO
 */
@Data
public class LeaveQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 请假类型
     */
    private Integer leaveType;

    /**
     * 审批状态
     */
    private Integer approvalStatus;

    /**
     * 开始日期（查询起始）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    /**
     * 结束日期（查询结束）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
