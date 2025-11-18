package com.example.businessservice.coursechange.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.businessservice.coursechange.dao.CourseChangeDao;
import com.example.businessservice.coursechange.dto.CourseChangeApplyRequest;
import com.example.businessservice.coursechange.dto.CourseChangeApproveRequest;
import com.example.businessservice.coursechange.dto.CourseChangeQueryRequest;
import com.example.businessservice.coursechange.entity.BizCourseChange;
import com.example.businessservice.coursechange.service.CourseChangeService;
import com.example.businessservice.coursechange.vo.CourseChangeVO;
import com.example.businessservice.enums.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 调课申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseChangeServiceImpl implements CourseChangeService {

    private final CourseChangeDao courseChangeDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyCourseChange(CourseChangeApplyRequest request) {
        log.info("提交调课申请: teacherId={}, originalDate={}, newDate={}",
                request.getApplyTeacherId(), request.getOriginalDate(), request.getNewDate());

        // 1. 参数校验
        validateCourseChangeRequest(request);

        // 2. 构建调课实体
        BizCourseChange courseChange = new BizCourseChange();
        BeanUtils.copyProperties(request, courseChange);

        // 3. 生成调课单号
        courseChange.setChangeNo(generateChangeNo());

        // 4. 设置申请时间和审批状态
        courseChange.setApplyTime(LocalDateTime.now());
        courseChange.setApprovalStatus(ApprovalStatus.PENDING.getCode());

        // 5. 保存到数据库
        courseChangeDao.insert(courseChange);

        log.info("调课申请提交成功: changeId={}, changeNo={}", courseChange.getId(), courseChange.getChangeNo());
        return courseChange.getId();
    }

    @Override
    public CourseChangeVO getCourseChangeDetail(Long id) {
        log.info("查询调课详情: id={}", id);

        CourseChangeVO courseChangeVO = courseChangeDao.selectCourseChangeDetailById(id);
        if (courseChangeVO == null) {
            throw new IllegalArgumentException("调课记录不存在");
        }

        // 设置枚举描述
        enrichCourseChangeVO(courseChangeVO);

        return courseChangeVO;
    }

    @Override
    public IPage<CourseChangeVO> getCourseChangeList(CourseChangeQueryRequest request) {
        log.info("分页查询调课列表: request={}", request);

        Page<CourseChangeVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        IPage<CourseChangeVO> result = courseChangeDao.selectCourseChangePage(
                page,
                request.getApplyTeacherId(),
                request.getApprovalStatus(),
                request.getStartDate(),
                request.getEndDate()
        );

        // 设置枚举描述
        result.getRecords().forEach(this::enrichCourseChangeVO);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelCourseChange(Long id, Long teacherId) {
        log.info("撤回调课申请: id={}, teacherId={}", id, teacherId);

        // 1. 查询调课记录
        BizCourseChange courseChange = courseChangeDao.selectById(id);
        if (courseChange == null) {
            throw new IllegalArgumentException("调课记录不存在");
        }

        // 2. 校验权限
        if (!courseChange.getApplyTeacherId().equals(teacherId)) {
            throw new IllegalArgumentException("无权撤回他人的调课申请");
        }

        // 3. 校验状态（只有待审批状态可以撤回）
        if (!ApprovalStatus.PENDING.getCode().equals(courseChange.getApprovalStatus())) {
            throw new IllegalArgumentException("当前状态不允许撤回");
        }

        // 4. 更新状态为已撤回
        courseChange.setApprovalStatus(ApprovalStatus.CANCELLED.getCode());
        int rows = courseChangeDao.updateById(courseChange);

        log.info("调课申请撤回成功: id={}", id);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveCourseChange(CourseChangeApproveRequest request) {
        log.info("审批调课申请: changeId={}, result={}", request.getChangeId(), request.getApprovalResult());

        // 1. 查询调课记录
        BizCourseChange courseChange = courseChangeDao.selectById(request.getChangeId());
        if (courseChange == null) {
            throw new IllegalArgumentException("调课记录不存在");
        }

        // 2. 校验状态（只有待审批状态可以审批）
        if (!ApprovalStatus.PENDING.getCode().equals(courseChange.getApprovalStatus())) {
            throw new IllegalArgumentException("当前状态不允许审批");
        }

        // 3. 校验审批结果
        if (!ApprovalStatus.APPROVED.getCode().equals(request.getApprovalResult())
                && !ApprovalStatus.REJECTED.getCode().equals(request.getApprovalResult())) {
            throw new IllegalArgumentException("审批结果不合法");
        }

        // 4. 更新审批状态
        courseChange.setApprovalStatus(request.getApprovalResult());
        int rows = courseChangeDao.updateById(courseChange);

        // TODO: 如果审批通过，需要更新edu_schedule课表

        log.info("调课申请审批成功: changeId={}, result={}", request.getChangeId(), request.getApprovalResult());
        return rows > 0;
    }

    @Override
    public IPage<CourseChangeVO> getMyCourseChangeList(Long teacherId, Integer pageNum, Integer pageSize) {
        log.info("查询我的调课记录: teacherId={}, pageNum={}, pageSize={}", teacherId, pageNum, pageSize);

        Page<CourseChangeVO> page = new Page<>(pageNum, pageSize);

        IPage<CourseChangeVO> result = courseChangeDao.selectCourseChangePage(
                page,
                teacherId,
                null,
                null,
                null
        );

        // 设置枚举描述
        result.getRecords().forEach(this::enrichCourseChangeVO);

        return result;
    }

    /**
     * 校验调课申请参数
     */
    private void validateCourseChangeRequest(CourseChangeApplyRequest request) {
        if (request.getApplyTeacherId() == null) {
            throw new IllegalArgumentException("教师ID不能为空");
        }
        if (request.getOriginalScheduleId() == null) {
            throw new IllegalArgumentException("原课程表ID不能为空");
        }
        if (request.getOriginalDate() == null || request.getNewDate() == null) {
            throw new IllegalArgumentException("上课日期不能为空");
        }
        if (request.getOriginalPeriod() == null || request.getNewPeriod() == null) {
            throw new IllegalArgumentException("上课节次不能为空");
        }
        if (request.getOriginalDate().equals(request.getNewDate())
                && request.getOriginalPeriod().equals(request.getNewPeriod())) {
            throw new IllegalArgumentException("调课前后的时间不能相同");
        }
    }

    /**
     * 生成调课单号
     * 格式: CHANGE + yyyyMMddHHmmss + 4位随机数
     */
    private String generateChangeNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "CHANGE" + timestamp + random;
    }

    /**
     * 丰富CourseChangeVO对象（设置枚举描述）
     */
    private void enrichCourseChangeVO(CourseChangeVO vo) {
        // 设置审批状态描述
        ApprovalStatus approvalStatus = ApprovalStatus.getByCode(vo.getApprovalStatus());
        if (approvalStatus != null) {
            vo.setApprovalStatusDesc(approvalStatus.getDesc());
        }
    }
}
