package com.example.fallback;

import com.example.client.AuthFeignClient;
import com.example.fallback.impl.AuthFeignClientImpl;
import org.springframework.cloud.openfeign.FallbackFactory;

public class AuthFeignClientFallbackFactory implements FallbackFactory<AuthFeignClient> {
    @Override
    public AuthFeignClient create(Throwable cause) {
        return new AuthFeignClientImpl(cause);
    }
}
