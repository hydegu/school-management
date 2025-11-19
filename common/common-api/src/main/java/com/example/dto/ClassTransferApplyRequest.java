package com.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 调班申请请求DTO
 */
@Data
public class ClassTransferApplyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long studentId;
    private String studentName;
    private Long originalClassId;
    private String originalClassName;
    private Long targetClassId;
    private String targetClassName;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate effectiveDate;

    private String remark;
}
