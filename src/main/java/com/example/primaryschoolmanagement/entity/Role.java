package com.example.primaryschoolmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.primaryschoolmanagement.common.base.EntityBase;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("sys_role")
@Accessors(chain = true)
public class Role extends EntityBase {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String roleName;
    private String roleCode;
    private String roleDesc;
    private String sortOrder;
    private Boolean status;
}
