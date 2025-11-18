package com.example.businessservice.service;

import com.example.businessservice.dto.ClassTransferApplyRequest;

/**
 * 调班申请服务接口
 */
public interface ClassTransferService {
    Long applyClassTransfer(ClassTransferApplyRequest request);
}
