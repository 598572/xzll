package com.xzll.springboot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 授权服务器 --- 演示使用数据库(redis)存储token信息 ;客户端信息(client-id和client-sercert) 还是在内存中
 * ps:(本示例基于用户名密码模式)
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//使用redis 所以需要把数据库排除
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
