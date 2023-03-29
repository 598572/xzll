package com.xzll.test;

import com.xzll.common.rabbitmq.EnableRabbitMq;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.xzll")
//通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
@EnableDiscoveryClient
@EnableFeignClients
//@EnableTransactionManagement
//@EnableAspectJAutoProxy(exposeProxy = true)//exposeProxy 直译为 暴露代理
@EnableRetry
@EnableScheduling
@EnableRabbitMq
@MapperScan(basePackages = {"com.xzll.*.mapper"})//由于在common模块中也有mapper （用于保存未投递/消费成功的消息）所以此处需要配置包扫描路径，确保common中的mapper也被spring扫描并管理
public class StudyTestApplication {


    public static void main(String[] args) {
        SpringApplication.run(StudyTestApplication.class, args);
    }

}
