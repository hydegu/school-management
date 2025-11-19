package com.example.teacherservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 教师服务启动类
 * 负责教师信息管理
 */
@SpringBootApplication(scanBasePackages = {"com.example.teacherservice", "com.example"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.client")
@MapperScan("com.example.teacherservice.dao")
public class TeacherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherServiceApplication.class, args);
    }

}
