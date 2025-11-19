package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置 - WebFlux版本
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许的源（开发环境可以使用 "*"，生产环境应该指定具体域名）
        corsConfiguration.addAllowedOriginPattern("*");

        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");

        // 允许的HTTP方法
        corsConfiguration.addAllowedMethod("*");

        // 允许携带认证信息（如Cookie）
        corsConfiguration.setAllowCredentials(true);

        // 预检请求的有效期（秒）
        corsConfiguration.setMaxAge(3600L);

        // 暴露的响应头
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("Content-Type");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}
