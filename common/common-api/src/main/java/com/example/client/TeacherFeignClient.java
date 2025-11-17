package com.example.client;

import com.example.fallback.TeacherFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "teacher-service",
        path = "/api/teacher",
        fallbackFactory = TeacherFeignClientFallbackFactory.class
)
public interface TeacherFeignClient {
}
