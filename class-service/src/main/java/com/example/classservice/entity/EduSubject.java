package com.example.classservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 科目实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("edu_subject")
@Accessors(chain = true)
public class EduSubject extends BaseEntity {

    /**
     * 科目ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 科目名称（语文/数学/英语/体育/微机/音乐/道法/科学）
     */
    private String subjectName;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 显示排序
     */
    private Integer sortOrder;

    /**
     * 状态：0-停用 1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
