package com.example.fallback;

import com.example.client.BusinessFeignClient;
import com.example.fallback.impl.BusinessFeignClientImpl;
import org.springframework.cloud.openfeign.FallbackFactory;

public class BusinessFeignClientFallbackFactory implements FallbackFactory<BusinessFeignClient> {
    @Override
    public BusinessFeignClient create(Throwable cause) {
        return new BusinessFeignClientImpl(cause);
    }
}
