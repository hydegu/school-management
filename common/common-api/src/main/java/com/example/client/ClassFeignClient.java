package com.example.client;

import com.example.fallback.ClassFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "class-service",
        path = "/api/class",
        fallbackFactory = ClassFeignClientFallbackFactory.class
)
public interface ClassFeignClient {
}
