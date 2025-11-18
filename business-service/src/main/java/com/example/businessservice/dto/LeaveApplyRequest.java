package com.example.businessservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 请假申请请求DTO
 */
@Data
public class LeaveApplyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 请假类型：1-病假 2-事假 3-其他
     */
    private Integer leaveType;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    /**
     * 请假天数
     */
    private BigDecimal leaveDays;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 证明材料URL列表
     */
    private List<String> proofFiles;

    /**
     * 备注
     */
    private String remark;
}
