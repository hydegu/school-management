package com.example.businessservice.coursechange.controller;

import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 调课申请控制器
 * TODO: 完整实现参考LeaveController
 */
@Slf4j
@RestController
@RequestMapping("/api/business/course-change")
@RequiredArgsConstructor
public class CourseChangeController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public R<String> health() {
        return R.ok("CourseChange模块运行正常");
    }
}
