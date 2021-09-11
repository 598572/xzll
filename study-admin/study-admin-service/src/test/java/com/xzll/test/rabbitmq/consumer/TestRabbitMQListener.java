package com.xzll.test.rabbitmq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xzll.common.rabbitmq.AbstractRabbitMQListener;
import com.xzll.test.rabbitmq.TestMqConstant;
import com.xzll.test.rabbitmq.UserMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: hzz
 * @Date: 2021/9/10 17:43:52
 * @Description: 测试RabbitMQ
 */
@Component
@Slf4j
public class TestRabbitMQListener extends AbstractRabbitMQListener<UserMQ> {

    /**
     * 1. 创建交换机
     * @return
     */
    @Bean
    public DirectExchange testDirectExchange(){
        DirectExchange directExchange = new DirectExchange(TestMqConstant.TEST_EX);
        return directExchange;
    }
    /**
     * 2. 创建队列
     * @return
     */
    @Bean
    public Queue testQueue(){
        return new Queue(TestMqConstant.TEST_Q,true,false,false);
    }

    /**
     * 3. 绑定队列到交换机 并指定routingKey
     * @return
     */
    @Bean
    public Binding bindingTestDirectQueue() {
        return BindingBuilder.bind(testQueue()).to(testDirectExchange()).with(TestMqConstant.TEST_RK);
    }

	/**
	 * 监听 某个queue 支持多个
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
    @Override
    @RabbitListener(queues = TestMqConstant.TEST_Q)
    protected void receiveMessage(Message message, Channel channel) throws Exception {
        super.receiveMessage(message,channel);
    }

	/**
	 * 该方法被 receiveMessage调用。 用于执行 正常逻辑
	 * @param content
	 * @throws Exception
	 */
	@Override
	protected void successExecuteHook(UserMQ content) throws Exception {
		log.info("开始消费消息，{}",JSONObject.toJSONString(content));

	}

	/**
	 * 该方法被 receiveMessage调用。 用于执行 异常逻辑
	 * @param content
	 */
	@Override
	protected void failExecuteHook(UserMQ content) {
		log.error("消费数据出错：{}", JSONObject.toJSONString(content));
	}
}
