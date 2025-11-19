package com.example.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthSecurityConfig {

    @Bean
    @Order(1)  // ⚠️ 优先级高于 common 的配置
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/auth/**")  // 只匹配 /api/auth/** 路径
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()  // 登录接口公开
                )
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // PasswordEncoder 已在 JwtSecurityConfig (common-security) 中定义，这里移除重复定义
}
