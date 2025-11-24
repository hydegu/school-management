package com.example.client;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RefreshRequest;
import com.example.fallback.AuthFeignClientFallbackFactory;
import com.example.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        path = "/api/auth",
        fallbackFactory = AuthFeignClientFallbackFactory.class
)
public interface AuthFeignClient {

    @PostMapping("/login")
    public R<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response);

    @PostMapping("/refresh")
    public R<AuthResponse> refresh(@RequestBody(required = false) RefreshRequest refreshRequest,
                                   HttpServletRequest request,
                                   HttpServletResponse response);

    @PostMapping("/logout")
    public R<String> logout(HttpServletResponse response);
}
