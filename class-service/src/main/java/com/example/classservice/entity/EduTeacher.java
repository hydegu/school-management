package com.example.classservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 教师实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_teacher")
@Accessors(chain = true)
public class EduTeacher extends BaseEntity {

    /**
     * 教师ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 教师编号
     */
    private String teacherNo;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 性别：1-男 2-女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthDate;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 职称（如：班主任/语文老师）
     */
    private String title;

    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate hireDate;
}
