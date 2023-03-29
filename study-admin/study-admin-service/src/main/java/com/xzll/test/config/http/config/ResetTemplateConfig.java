package com.xzll.test.config.http.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @Author: hzz
 * @Date: 2021/9/3 12:47:09
 * @Description:
 */
@Component
public class ResetTemplateConfig {

	@Autowired
	private RestTemplateBuilder builder;


	@Bean
	@Primary
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = builder.build();
		//为 RestTemplate 设置拦截器 添加traceId
		restTemplate.setInterceptors(Arrays.asList(new RestTemplateTraceIdInterceptor()));
		return restTemplate;
	}

}
