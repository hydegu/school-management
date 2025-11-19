package com.example.classservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 年级实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_grade")
@Accessors(chain = true)
public class EduGrade extends BaseEntity {

    /**
     * 年级ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 年级名称（如：一年级）
     */
    private String gradeName;

    /**
     * 年级级别：1-6
     */
    private Integer gradeLevel;

    /**
     * 学年（如：2023-2024）
     */
    private String schoolYear;

    /**
     * 状态：0-停用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
