package com.xzll.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: hzz
 * @Date: 2023/11/10 16:32:16
 * @Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * 后端跨域配置
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowCredentials(true)
				.allowedHeaders("*")
				.allowedMethods("GET", "POST", "DELETE", "PUT")
				.maxAge(3600);
	}
}
