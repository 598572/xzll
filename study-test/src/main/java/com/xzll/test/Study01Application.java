package com.xzll.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Study01Application {

    public static void main(String[] args) {
        SpringApplication.run(Study01Application.class, args);
    }

}
