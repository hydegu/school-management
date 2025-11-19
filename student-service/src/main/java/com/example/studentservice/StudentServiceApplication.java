package com.example.studentservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 学生服务启动类
 * 负责学生个人事务管理
 */
@SpringBootApplication(scanBasePackages = {"com.example.studentservice", "com.example"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.client")
@MapperScan("com.example.studentservice.dao")
public class StudentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }

}
