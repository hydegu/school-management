package com.example.fallback;

import com.example.client.ClassFeignClient;
import com.example.fallback.impl.ClassFeignClientImpl;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ClassFeignClientFallbackFactory implements FallbackFactory<ClassFeignClient> {
    @Override
    public ClassFeignClient create(Throwable cause) {
        return new ClassFeignClientImpl(cause);
    }
}
