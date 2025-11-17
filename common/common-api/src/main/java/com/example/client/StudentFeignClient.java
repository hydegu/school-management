package com.example.client;

import com.example.fallback.StudentFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "student-service",
        path = "/api/student",
        fallbackFactory = StudentFeignClientFallbackFactory.class
)
public interface StudentFeignClient {
}
