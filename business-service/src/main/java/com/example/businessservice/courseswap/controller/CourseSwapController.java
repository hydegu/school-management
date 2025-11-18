package com.example.businessservice.courseswap.controller;

import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 换课申请控制器
 * TODO: 完整实现参考LeaveController，需增加对方确认功能
 */
@Slf4j
@RestController
@RequestMapping("/api/business/course-swap")
@RequiredArgsConstructor
public class CourseSwapController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public R<String> health() {
        return R.ok("CourseSwap模块运行正常");
    }
}
