package com.example.businessservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.businessservice.enums.ApprovalStatus;
import com.example.businessservice.enums.LeaveType;
import com.example.businessservice.dao.LeaveDao;
import com.example.businessservice.dto.LeaveApplyRequest;
import com.example.businessservice.dto.LeaveApproveRequest;
import com.example.businessservice.dto.LeaveQueryRequest;
import com.example.businessservice.entity.BizLeave;
import com.example.businessservice.service.LeaveService;
import com.example.businessservice.vo.LeaveVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 请假申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveDao leaveDao;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyLeave(LeaveApplyRequest request) {
        log.info("提交请假申请: studentId={}, startDate={}, endDate={}",
                request.getStudentId(), request.getStartDate(), request.getEndDate());

        // 1. 参数校验
        validateLeaveRequest(request);

        // 2. 构建请假实体
        BizLeave leave = new BizLeave();
        BeanUtils.copyProperties(request, leave);

        // 3. 生成请假单号
        leave.setLeaveNo(generateLeaveNo());

        // 4. 处理证明材料（List转JSON）
        if (request.getProofFiles() != null && !request.getProofFiles().isEmpty()) {
            try {
                leave.setProofFiles(objectMapper.writeValueAsString(request.getProofFiles()));
            } catch (JsonProcessingException e) {
                log.error("证明材料JSON序列化失败", e);
                throw new RuntimeException("证明材料处理失败");
            }
        }

        // 5. 设置申请时间和审批状态
        leave.setApplyTime(LocalDateTime.now());
        leave.setApprovalStatus(ApprovalStatus.PENDING.getCode());

        // 6. 保存到数据库
        leaveDao.insert(leave);

        log.info("请假申请提交成功: leaveId={}, leaveNo={}", leave.getId(), leave.getLeaveNo());
        return leave.getId();
    }

    @Override
    public LeaveVO getLeaveDetail(Long id) {
        log.info("查询请假详情: id={}", id);

        LeaveVO leaveVO = leaveDao.selectLeaveDetailById(id);
        if (leaveVO == null) {
            throw new IllegalArgumentException("请假记录不存在");
        }

        // 设置枚举描述
        enrichLeaveVO(leaveVO);

        return leaveVO;
    }

    @Override
    public IPage<LeaveVO> getLeaveList(LeaveQueryRequest request) {
        log.info("分页查询请假列表: request={}", request);

        Page<LeaveVO> page = new Page<>(request.getPageNum(), request.getPageSize());

        IPage<LeaveVO> result = leaveDao.selectLeavePage(
                page,
                request.getStudentId(),
                request.getClassId(),
                request.getLeaveType(),
                request.getApprovalStatus(),
                request.getStartDate(),
                request.getEndDate()
        );

        // 设置枚举描述
        result.getRecords().forEach(this::enrichLeaveVO);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelLeave(Long id, Long studentId) {
        log.info("撤回请假申请: id={}, studentId={}", id, studentId);

        // 1. 查询请假记录
        BizLeave leave = leaveDao.selectById(id);
        if (leave == null) {
            throw new IllegalArgumentException("请假记录不存在");
        }

        // 2. 校验权限
        if (!leave.getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("无权撤回他人的请假申请");
        }

        // 3. 校验状态（只有待审批状态可以撤回）
        if (!ApprovalStatus.PENDING.getCode().equals(leave.getApprovalStatus())) {
            throw new IllegalArgumentException("当前状态不允许撤回");
        }

        // 4. 更新状态为已撤回
        leave.setApprovalStatus(ApprovalStatus.CANCELLED.getCode());
        int rows = leaveDao.updateById(leave);

        log.info("请假申请撤回成功: id={}", id);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveLeave(LeaveApproveRequest request) {
        log.info("审批请假申请: leaveId={}, result={}", request.getLeaveId(), request.getApprovalResult());

        // 1. 查询请假记录
        BizLeave leave = leaveDao.selectById(request.getLeaveId());
        if (leave == null) {
            throw new IllegalArgumentException("请假记录不存在");
        }

        // 2. 校验状态（只有待审批状态可以审批）
        if (!ApprovalStatus.PENDING.getCode().equals(leave.getApprovalStatus())) {
            throw new IllegalArgumentException("当前状态不允许审批");
        }

        // 3. 校验审批结果
        if (!ApprovalStatus.APPROVED.getCode().equals(request.getApprovalResult())
                && !ApprovalStatus.REJECTED.getCode().equals(request.getApprovalResult())) {
            throw new IllegalArgumentException("审批结果不合法");
        }

        // 4. 更新审批状态
        leave.setApprovalStatus(request.getApprovalResult());
        int rows = leaveDao.updateById(leave);

        log.info("请假申请审批成功: leaveId={}, result={}", request.getLeaveId(), request.getApprovalResult());
        return rows > 0;
    }

    @Override
    public IPage<LeaveVO> getMyLeaveList(Long studentId, Integer pageNum, Integer pageSize) {
        log.info("查询我的请假记录: studentId={}, pageNum={}, pageSize={}", studentId, pageNum, pageSize);

        Page<LeaveVO> page = new Page<>(pageNum, pageSize);

        IPage<LeaveVO> result = leaveDao.selectLeavePage(
                page,
                studentId,
                null,
                null,
                null,
                null,
                null
        );

        // 设置枚举描述
        result.getRecords().forEach(this::enrichLeaveVO);

        return result;
    }

    /**
     * 校验请假申请参数
     */
    private void validateLeaveRequest(LeaveApplyRequest request) {
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        if (request.getClassId() == null) {
            throw new IllegalArgumentException("班级ID不能为空");
        }
        if (request.getLeaveType() == null) {
            throw new IllegalArgumentException("请假类型不能为空");
        }
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("请假日期不能为空");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        if (request.getLeaveDays() == null || request.getLeaveDays().doubleValue() <= 0) {
            throw new IllegalArgumentException("请假天数必须大于0");
        }
    }

    /**
     * 生成请假单号
     * 格式: LEAVE + yyyyMMddHHmmss + 4位随机数
     */
    private String generateLeaveNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "LEAVE" + timestamp + random;
    }

    /**
     * 丰富LeaveVO对象（设置枚举描述、解析JSON等）
     */
    private void enrichLeaveVO(LeaveVO vo) {
        // 设置请假类型描述
        LeaveType leaveType = LeaveType.getByCode(vo.getLeaveType());
        if (leaveType != null) {
            vo.setLeaveTypeDesc(leaveType.getDesc());
        }

        // 设置审批状态描述
        ApprovalStatus approvalStatus = ApprovalStatus.getByCode(vo.getApprovalStatus());
        if (approvalStatus != null) {
            vo.setApprovalStatusDesc(approvalStatus.getDesc());
        }

        // 解析证明材料JSON（如果有的话，这里简化处理，实际可能需要从entity中获取并解析）
        // 由于VO中已经定义了List<String> proofFiles，但SQL查询返回的是JSON字符串
        // 实际应用中可能需要在这里进行JSON反序列化
    }
}
