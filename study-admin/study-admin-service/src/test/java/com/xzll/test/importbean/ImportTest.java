package com.xzll.test.importbean;

import com.xzll.common.alarm.service.AlarmNotice;
import com.xzll.common.email.EmailService;
import com.xzll.common.email.config.RedisTemplateExample;
import com.xzll.common.email.selector.TestRegisterBeanDefinitions;
import com.xzll.test.StudyTestApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/17 15:51:28
 * @Description:
 */
@Slf4j
public class ImportTest extends StudyTestApplicationTest {

	@Autowired
	private EmailService emailService;

	@Autowired
	private RedisTemplateExample redisTemplateExample;

	@Autowired
	private Map<String, AlarmNotice> alarmNoticeMap;
	@Autowired
	@Qualifier(value = "hzzxzll")
	private TestRegisterBeanDefinitions testRegisterBeanDefinitions;


	@Test
	public void test() {
		//log.info(" emailService: {}",emailService);
		//log.info(" redisTemplateExample: {}",redisTemplateExample);
//		alarmNoticeMap.forEach((key, value) -> {
//			log.info("beanName :{},bean:{}", key, value);
//		});
		log.info("手动注入bean:{}", testRegisterBeanDefinitions);

	}
}
