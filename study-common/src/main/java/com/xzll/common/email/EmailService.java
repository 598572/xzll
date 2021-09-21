package com.xzll.common.email;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: hzz
 * @Date: 2021/9/18 16:11:54
 * @Description:
 */
@Slf4j
public class EmailService {

	public void sendEmail(String content) {
		log.info(" 【 {} 发送短信: {} 】", EmailService.class.getSimpleName(), content);
	}
}
