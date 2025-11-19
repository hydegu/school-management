package com.example.workflowservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 审批流程服务启动类
 * 负责审批流程管理
 */
@SpringBootApplication(scanBasePackages = {"com.example.workflowservice", "com.example"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.client")
@MapperScan("com.example.workflowservice.dao")
public class WorkflowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowServiceApplication.class, args);
    }

}
