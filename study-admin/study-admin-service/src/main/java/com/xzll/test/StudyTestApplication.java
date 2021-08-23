package com.xzll.test;

import com.xzll.test.point.ApplicationContextInitializerPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.xzll")
//通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
@EnableDiscoveryClient
@EnableFeignClients
//@EnableTransactionManagement
//@EnableAspectJAutoProxy
public class StudyTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyTestApplication.class, args);
    }

}
