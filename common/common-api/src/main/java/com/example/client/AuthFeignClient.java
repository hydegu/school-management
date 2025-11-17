package com.example.client;


import com.example.fallback.AuthFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "auth-service",
        path = "/api/auth",
        fallbackFactory = AuthFeignClientFallbackFactory.class
)
public interface AuthFeignClient {

}
