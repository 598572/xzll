package com.xzll.springboot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 授权服务器 --- 演示获取refreshToekn用于accessToken的续期
 *
 *
 *
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
