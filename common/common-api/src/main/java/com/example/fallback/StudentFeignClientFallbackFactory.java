package com.example.fallback;


import com.example.client.StudentFeignClient;
import com.example.fallback.impl.StudentFeignClientImpl;
import org.springframework.cloud.openfeign.FallbackFactory;

public class StudentFeignClientFallbackFactory implements FallbackFactory<StudentFeignClient> {
    @Override
    public StudentFeignClient create(Throwable cause) {
        return new StudentFeignClientImpl(cause);
    }
}
