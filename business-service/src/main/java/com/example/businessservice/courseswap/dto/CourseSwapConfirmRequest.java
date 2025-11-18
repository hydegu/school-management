package com.example.businessservice.courseswap.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 换课确认请求DTO（目标教师确认）
 */
@Data
public class CourseSwapConfirmRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 换课ID
     */
    private Long swapId;

    /**
     * 目标教师ID
     */
    private Long targetTeacherId;

    /**
     * 确认结果：1-同意 2-拒绝
     */
    private Integer confirmResult;

    /**
     * 确认意见
     */
    private String confirmOpinion;
}
