package com.example.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration

@ComponentScan(basePackages = {

        "com.example.config",   // 扫描 config 包：JwtSecurityConfig, JwtAuthenticationFilter, JwtAuthenticationEntryPoint

        "com.example.handler"   // 扫描 handler 包：JwtAccessDeniedHandler

})

public class SecurityAutoConfiguration {

    // 自动配置类，无需额外代码

}
