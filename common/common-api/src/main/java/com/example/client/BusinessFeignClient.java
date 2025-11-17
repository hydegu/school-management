package com.example.client;

import com.example.fallback.BusinessFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "business-service",
        path = "/api/business",
        fallbackFactory = BusinessFeignClientFallbackFactory.class
)
public interface BusinessFeignClient {
}
