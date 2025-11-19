package com.example.fallback.impl;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.LogoutRequest;
import com.example.dto.RefreshRequest;
import com.example.client.AuthFeignClient;
import com.example.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
public class AuthFeignClientImpl implements AuthFeignClient {
    private final Throwable cause;

    @Override
    public R<AuthResponse> login(LoginRequest request, HttpServletResponse response) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<AuthResponse> refresh(@RequestBody(required = false) RefreshRequest refreshRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        return R.error(500, cause.getMessage());
    }

    @Override
    public R<String> logout(@RequestBody LogoutRequest request, HttpServletResponse response) {
        return R.error(500, cause.getMessage());
    }
}
