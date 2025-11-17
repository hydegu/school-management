package com.example.fallback.impl;

import com.example.client.ClassFeignClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClassFeignClientImpl implements ClassFeignClient {
    private final Throwable cause;
}
