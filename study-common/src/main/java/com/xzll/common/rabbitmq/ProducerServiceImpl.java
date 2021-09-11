package com.xzll.common.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author: hzz
 * @Date: 2021/9/10 12:53:33
 * @Description:
 */
public class ProducerServiceImpl implements ProducerService{

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	/**
	 * 消息发送封装
	 *
	 * @param content
	 * @param exchangeName
	 * @param routingKey
	 */
	@Override
	public void sendMsg (MqMessage content, String exchangeName, String routingKey){
		Message message = MessageBuilder.withBody(JSONObject.toJSONString(content).getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				//设置消息id
				.setCorrelationId(content.getId())
				.build();
		if(StringUtils.isNotBlank(content.getExpiration())){
			//为消息设置过期时间
			message = MessageBuilder.fromMessage(message).setExpiration(content.getExpiration()).build();
		}
		CorrelationData data = new CorrelationData(content.getId());
		//存储到redis
		redisTemplate.opsForValue().set(data.getId(),JSONObject.toJSONString(content));
		rabbitTemplate.convertAndSend(exchangeName,routingKey,message,data);
	}
}
