package com.xzll.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: hzz
 * @Date: 2023/3/1 15:23:51
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.xzll")
public class StudyOrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudyOrderApplication.class, args);
	}
}
