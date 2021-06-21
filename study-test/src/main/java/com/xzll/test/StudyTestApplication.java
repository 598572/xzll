package com.xzll.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.xzll")
//通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
@EnableDiscoveryClient
@EnableFeignClients
public class StudyTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyTestApplication.class, args);
    }

//    @RestController
//    class EchoController {
//        @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
//        public String echo(@PathVariable String string) {
//            return "Hello Nacos Discovery " + string;
//        }
//    }
}