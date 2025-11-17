package com.example.fallback.impl;

import com.example.client.StudentFeignClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StudentFeignClientImpl implements StudentFeignClient {
    private final Throwable cause;
}
