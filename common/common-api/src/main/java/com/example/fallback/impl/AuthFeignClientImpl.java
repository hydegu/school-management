package com.example.fallback.impl;

import com.example.client.AuthFeignClient;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthFeignClientImpl implements AuthFeignClient {
    private final Throwable cause;

}
