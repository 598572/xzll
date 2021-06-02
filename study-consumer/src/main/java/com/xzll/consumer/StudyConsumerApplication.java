package com.xzll.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.xzll")
@EnableDiscoveryClient
@EnableFeignClients
public class StudyConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyConsumerApplication.class, args);
    }

}
