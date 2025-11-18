package com.example.businessservice.controller;

import com.example.businessservice.dto.ClassTransferApplyRequest;
import com.example.businessservice.service.ClassTransferService;
import com.example.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 调班申请控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/business/class-transfer")
@RequiredArgsConstructor
public class ClassTransferController {

    private final ClassTransferService classTransferService;

    /**
     * 提交调班申请
     *
     * @param request 调班申请请求
     * @return 调班ID
     */
    @PostMapping
    public R<Long> applyClassTransfer(@RequestBody ClassTransferApplyRequest request) {
        try {
            Long transferId = classTransferService.applyClassTransfer(request);
            return R.ok("调班申请提交成功", transferId);
        } catch (IllegalArgumentException e) {
            log.warn("调班申请失败: {}", e.getMessage());
            return R.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("调班申请异常: ", e);
            return R.error("调班申请失败");
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public R<String> health() {
        return R.ok("ClassTransfer模块运行正常");
    }
}
