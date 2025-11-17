package com.example.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Security自动配置 - 支持双令牌机制
 */
@AutoConfiguration
@ComponentScan(basePackages = {
        "com.example.config",   // 扫描config包：JwtSecurityConfig, JwtProperties
        "com.example.filter",   // 扫描filter包：JwtAuthenticationFilter, JwtAuthenticationEntryPoint
        "com.example.handler",  // 扫描handler包：JwtAccessDeniedHandler
        "com.example.service"   // 扫描service包：TokenService
})
public class SecurityAutoConfiguration {
    // 自动配置类，无需额外代码
}
