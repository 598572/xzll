package com.xzll.common.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: hzz
 * @Date: 2021/9/18 16:32:40
 * @Description: 假装这是个redis配置类，用于
 * 初始化redis模板，redis序列化，等等等等
 */
@Configuration
public class RedisConfigurationExample {

	@Bean
	public RedisTemplateExample createBean(){
		return new RedisTemplateExample();
	}
}
