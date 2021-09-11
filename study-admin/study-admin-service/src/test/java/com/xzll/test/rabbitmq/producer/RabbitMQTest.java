package com.xzll.test.rabbitmq.producer;

import com.xzll.common.rabbitmq.producer.ProducerService;
import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.rabbitmq.TestMqConstant;
import com.xzll.test.rabbitmq.UserMQ;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: hzz
 * @Date: 2021/9/10 12:53:33
 * @Description: 测试RabbitMQ
 */
@Slf4j
public class RabbitMQTest extends StudyTestApplicationTest {

    @Autowired
	private ProducerService producerService;

    @Test
	public void sendMessage(){


		UserMQ userMQ = new UserMQ();
		userMQ.setAddress("张家口");
		userMQ.setName("蝎子莱莱01");
		userMQ.setAge(29);
		userMQ.setSex(1);

		producerService.sendMsg(userMQ, TestMqConstant.TEST_EX,TestMqConstant.TEST_RK);

		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
