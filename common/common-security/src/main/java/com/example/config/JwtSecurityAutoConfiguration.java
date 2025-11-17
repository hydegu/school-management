package com.example.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@AutoConfiguration
@ConditionalOnClass(HttpSecurity.class)
@EnableConfigurationProperties(JwtProperties.class)
@ComponentScan(basePackages = {
        "com.example.config",
        "com.example.handler",
        "com.example.context"
})
public class JwtSecurityAutoConfiguration {
}
