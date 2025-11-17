package com.example.client;

import com.example.fallback.WorkFlowFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "work-flow-service",
        path = "/api/work-flow",
        fallbackFactory = WorkFlowFeignClientFallbackFactory.class
)
public interface WorkFlowFeignClient {
}
