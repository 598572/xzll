package com.xzll.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 资源服务器--模拟某系统调用授权服务器进行认证授权
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}
