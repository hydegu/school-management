package com.example.fallback;

import com.example.client.WorkFlowFeignClient;
import com.example.fallback.impl.WorkFlowFeignClientImpl;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkFlowFeignClientFallbackFactory implements FallbackFactory<WorkFlowFeignClient> {
    @Override
    public WorkFlowFeignClient create(Throwable cause) {
        return new WorkFlowFeignClientImpl(cause);
    }
}
