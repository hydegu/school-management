package com.example.classservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 教务核心服务启动类
 * 负责班级、学生、教师、课程、课表等核心教务数据管理
 */
@SpringBootApplication(scanBasePackages = {"com.example.classservice", "com.example"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.client", "com.example.classservice.feign"})
@MapperScan("com.example.classservice.dao")
public class ClassServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassServiceApplication.class, args);
    }

}
