package com.example.workflowservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批流程配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_process")
@Accessors(chain = true)
public class FlowProcess extends BaseEntity {

    /**
     * 流程ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程编码
     */
    private String processCode;

    /**
     * 流程类型：1-请假 2-调课 3-换课 4-调班
     */
    private Integer processType;

    /**
     * 流程描述
     */
    private String processDesc;

    /**
     * 状态：0-停用 1-启用
     */
    private Integer status;
}
