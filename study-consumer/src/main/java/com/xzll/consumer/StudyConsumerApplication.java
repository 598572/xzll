package com.xzll.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xzll")
public class StudyConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyConsumerApplication.class, args);
    }

}
