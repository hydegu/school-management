package com.example.businessservice.courseswap.service.impl;

import com.example.businessservice.courseswap.dao.CourseSwapDao;
import com.example.businessservice.courseswap.dto.CourseSwapApplyRequest;
import com.example.businessservice.courseswap.dto.CourseSwapApproveRequest;
import com.example.businessservice.courseswap.dto.CourseSwapConfirmRequest;
import com.example.businessservice.courseswap.entity.BizCourseSwap;
import com.example.businessservice.courseswap.service.CourseSwapService;
import com.example.businessservice.courseswap.vo.CourseSwapVO;
import com.example.businessservice.enums.ApprovalStatus;
import com.example.businessservice.enums.ConfirmStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 换课申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseSwapServiceImpl implements CourseSwapService {

    private final CourseSwapDao courseSwapDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyCourseSwap(CourseSwapApplyRequest request) {
        log.info("提交换课申请: applyTeacherId={}, targetTeacherId={}",
                request.getApplyTeacherId(), request.getTargetTeacherId());

        BizCourseSwap courseSwap = new BizCourseSwap();
        BeanUtils.copyProperties(request, courseSwap);

        courseSwap.setSwapNo(generateSwapNo());
        courseSwap.setApplyTime(LocalDateTime.now());
        courseSwap.setTargetConfirm(ConfirmStatus.UNCONFIRMED.getCode());
        courseSwap.setApprovalStatus(ApprovalStatus.PENDING.getCode());

        courseSwapDao.insert(courseSwap);

        log.info("换课申请提交成功: swapId={}", courseSwap.getId());
        return courseSwap.getId();
    }

    @Override
    public CourseSwapVO getCourseSwapDetail(Long id) {
        BizCourseSwap courseSwap = courseSwapDao.selectById(id);
        if (courseSwap == null) {
            throw new IllegalArgumentException("换课记录不存在");
        }

        CourseSwapVO vo = new CourseSwapVO();
        BeanUtils.copyProperties(courseSwap, vo);
        enrichVO(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelCourseSwap(Long id, Long teacherId) {
        BizCourseSwap courseSwap = courseSwapDao.selectById(id);
        if (courseSwap == null) {
            throw new IllegalArgumentException("换课记录不存在");
        }
        if (!courseSwap.getApplyTeacherId().equals(teacherId)) {
            throw new IllegalArgumentException("无权撤回他人的换课申请");
        }
        if (!ApprovalStatus.PENDING.getCode().equals(courseSwap.getApprovalStatus())) {
            throw new IllegalArgumentException("当前状态不允许撤回");
        }

        courseSwap.setApprovalStatus(ApprovalStatus.CANCELLED.getCode());
        return courseSwapDao.updateById(courseSwap) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmCourseSwap(CourseSwapConfirmRequest request) {
        log.info("目标教师确认换课: swapId={}, result={}", request.getSwapId(), request.getConfirmResult());

        BizCourseSwap courseSwap = courseSwapDao.selectById(request.getSwapId());
        if (courseSwap == null) {
            throw new IllegalArgumentException("换课记录不存在");
        }
        if (!courseSwap.getTargetTeacherId().equals(request.getTargetTeacherId())) {
            throw new IllegalArgumentException("只有目标教师可以确认");
        }
        if (!ConfirmStatus.UNCONFIRMED.getCode().equals(courseSwap.getTargetConfirm())) {
            throw new IllegalArgumentException("已确认过，无法重复操作");
        }

        courseSwap.setTargetConfirm(request.getConfirmResult());
        return courseSwapDao.updateById(courseSwap) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveCourseSwap(CourseSwapApproveRequest request) {
        BizCourseSwap courseSwap = courseSwapDao.selectById(request.getSwapId());
        if (courseSwap == null) {
            throw new IllegalArgumentException("换课记录不存在");
        }
        // 必须先确认才能审批
        if (!ConfirmStatus.CONFIRMED.getCode().equals(courseSwap.getTargetConfirm())) {
            throw new IllegalArgumentException("目标教师未确认，不能审批");
        }

        courseSwap.setApprovalStatus(request.getApprovalResult());
        return courseSwapDao.updateById(courseSwap) > 0;
    }

    private String generateSwapNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "SWAP" + timestamp + random;
    }

    private void enrichVO(CourseSwapVO vo) {
        ConfirmStatus confirmStatus = ConfirmStatus.getByCode(vo.getTargetConfirm());
        if (confirmStatus != null) {
            vo.setTargetConfirmDesc(confirmStatus.getDesc());
        }
        ApprovalStatus approvalStatus = ApprovalStatus.getByCode(vo.getApprovalStatus());
        if (approvalStatus != null) {
            vo.setApprovalStatusDesc(approvalStatus.getDesc());
        }
    }
}
