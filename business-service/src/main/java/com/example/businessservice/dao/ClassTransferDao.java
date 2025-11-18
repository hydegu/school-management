package com.example.businessservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.businessservice.entity.BizClassTransfer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调班申请Dao
 */
@Mapper
public interface ClassTransferDao extends BaseMapper<BizClassTransfer> {
}
