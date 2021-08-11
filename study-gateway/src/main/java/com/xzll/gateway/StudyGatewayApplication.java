package com.xzll.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关认证模块 目前是最初版本  将会添加动态路由 监控 鉴权 限流等功能
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StudyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyGatewayApplication.class, args);
    }

}
