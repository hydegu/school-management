package com.example.fallback.impl;

import com.example.client.WorkFlowFeignClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WorkFlowFeignClientImpl implements WorkFlowFeignClient {
    private final Throwable cause;
}
