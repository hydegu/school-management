package com.example.businessservice.service.impl;

import com.example.businessservice.dao.ClassTransferDao;
import com.example.businessservice.dto.ClassTransferApplyRequest;
import com.example.businessservice.entity.BizClassTransfer;
import com.example.businessservice.service.ClassTransferService;
import com.example.businessservice.enums.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 调班申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassTransferServiceImpl implements ClassTransferService {

    private final ClassTransferDao classTransferDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyClassTransfer(ClassTransferApplyRequest request) {
        log.info("提交调班申请: studentId={}, targetClassId={}", request.getStudentId(), request.getTargetClassId());

        BizClassTransfer classTransfer = new BizClassTransfer();
        BeanUtils.copyProperties(request, classTransfer);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        classTransfer.setTransferNo("TRANSFER" + timestamp + (int) (Math.random() * 9000 + 1000));
        classTransfer.setApplyTime(LocalDateTime.now());
        classTransfer.setApprovalStatus(ApprovalStatus.PENDING.getCode());

        classTransferDao.insert(classTransfer);

        log.info("调班申请提交成功: transferId={}", classTransfer.getId());
        return classTransfer.getId();
    }
}
