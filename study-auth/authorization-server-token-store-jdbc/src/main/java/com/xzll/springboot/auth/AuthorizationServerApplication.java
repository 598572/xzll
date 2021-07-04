package com.xzll.springboot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 授权服务器 --- 演示使用数据库(mysql)存储客户端信息(client-id和client-sercert) , 和token信息 ;
 * ps:(本示例基于用户名密码模式)
 *
 */
@SpringBootApplication
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
