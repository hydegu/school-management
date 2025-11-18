package com.example.businessservice.classtransfer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.businessservice.classtransfer.entity.BizClassTransfer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调班申请Dao
 */
@Mapper
public interface ClassTransferDao extends BaseMapper<BizClassTransfer> {
}
