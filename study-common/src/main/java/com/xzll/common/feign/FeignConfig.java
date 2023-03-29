//package com.xzll.common.feign;
//
//import com.xzll.common.rabbitmq.config.RabbitTemplateConfig;
//import com.xzll.common.rabbitmq.producer.ProducerService;
//import com.xzll.common.rabbitmq.producer.impl.ProducerServiceImpl;
//import org.springframework.context.annotation.Bean;
//
///**
// * @Author: hzz
// * @Date: 2021/9/10 12:27:55
// * @Description:
// */
//public class FeignConfig {
//
//
//	/**
//	 * 初始化 RabbitTemplateConfig
//	 * @return
//	 */
//	@Bean
//	public RabbitTemplateConfig rabbitTemplateConfig(){
//		RabbitTemplateConfig rabbitTemplateConfig = new RabbitTemplateConfig();
//		return rabbitTemplateConfig;
//	}
//
//	/**
//	 * 生产者
//	 * @return
//	 */
//	@Bean
//	public ProducerService producerService(){
//		ProducerServiceImpl producerService = new ProducerServiceImpl();
//		return producerService;
//	}
//
//
//}
