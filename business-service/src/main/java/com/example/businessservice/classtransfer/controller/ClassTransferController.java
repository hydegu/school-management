package com.example.businessservice.classtransfer.controller;

import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 调班申请控制器
 * TODO: 完整实现参考LeaveController，审批通过后需要更新学生班级信息
 */
@Slf4j
@RestController
@RequestMapping("/api/business/class-transfer")
@RequiredArgsConstructor
public class ClassTransferController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public R<String> health() {
        return R.ok("ClassTransfer模块运行正常");
    }
}
