package com.example.businessservice.classtransfer.service;

import com.example.businessservice.classtransfer.dto.ClassTransferApplyRequest;

/**
 * 调班申请服务接口
 */
public interface ClassTransferService {
    Long applyClassTransfer(ClassTransferApplyRequest request);
}
