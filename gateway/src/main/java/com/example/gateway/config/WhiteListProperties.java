package com.example.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 白名单配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "whitelist")
public class WhiteListProperties {

    /**
     * 白名单URL列表（不需要JWT认证的路径）
     */
    private List<String> urls = new ArrayList<>();
}
