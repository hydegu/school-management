package com.example.businessservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 业务服务启动类
 * 负责处理请假、调课、换课、调班等审批业务
 */
@SpringBootApplication(scanBasePackages = {"com.example"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.client"})
@MapperScan("com.example.businessservice.**.dao")
public class BusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessServiceApplication.class, args);
    }

}
