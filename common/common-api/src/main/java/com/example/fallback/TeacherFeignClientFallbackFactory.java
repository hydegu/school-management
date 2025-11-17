package com.example.fallback;

import com.example.client.TeacherFeignClient;
import com.example.fallback.impl.TeacherFeignClientImpl;
import org.springframework.cloud.openfeign.FallbackFactory;

public class TeacherFeignClientFallbackFactory implements FallbackFactory<TeacherFeignClient> {
    @Override
    public TeacherFeignClient create(Throwable cause) {
        return new TeacherFeignClientImpl(cause);
    }
}
