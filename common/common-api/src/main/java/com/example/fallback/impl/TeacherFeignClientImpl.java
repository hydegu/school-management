package com.example.fallback.impl;

import com.example.client.TeacherFeignClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TeacherFeignClientImpl implements TeacherFeignClient {
    private final Throwable cause;
}
