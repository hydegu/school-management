package com.example.fallback.impl;

import com.example.client.BusinessFeignClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BusinessFeignClientImpl implements BusinessFeignClient {
    private final Throwable cause;

}
