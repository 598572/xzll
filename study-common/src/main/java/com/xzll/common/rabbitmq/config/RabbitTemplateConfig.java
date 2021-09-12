package com.xzll.common.rabbitmq.config;

import com.xzll.common.rabbitmq.listener.nack.template.SaveNackMessage;
import com.xzll.common.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: hzz
 * @Date: 2021/9/10 13:09:58
 * @Description: 自定义确认机制
 */
@Slf4j
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static ThreadPoolExecutor saveNackMessageThread = ThreadUtil.getThreadPool(8, 16, 1000, "saveNackMessageThread");


	@PostConstruct
	public void init() {
		rabbitTemplate.setConfirmCallback(this::confirm);
		rabbitTemplate.setReturnCallback(this::returnedMessage);
	}


	/**
	 * 生产者 确认消息的配置
	 * 此函数为回调函数,用于confirm 确认
	 *
	 * @param correlationData 消息唯一ID
	 * @param ack             确认消息是否被MQ 接收,true是已被接收,false反之
	 * @param cause
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		//发送成功
		if (ack) {
			//不做处理，等待消费成功
			log.info(correlationData.getId() + "：发送成功");
		} else {
			//持久化到数据库     测试该逻辑时候 把上边的if(ack) 改成if(!ack)即可
			log.error(correlationData.getId() + "：发送失败");
			log.info("备份内容：" + redisTemplate.opsForValue().get(correlationData.getId()));
			try {
				SaveNackMessage strategy = SaveNackMessage.getStrategy(SaveNackMessage.NackTypeEnum.PRODUCER.getType());
				HashMap<String, Object> map = new HashMap<>();
				map.put("cause", StringUtils.isNoneBlank(cause) ? cause : StringUtils.EMPTY);
				map.put("ack", ack ? 1 : 0);
				map.put("correlationData", Objects.nonNull(correlationData) ? correlationData : StringUtils.EMPTY);
				saveNackMessageThread.execute(strategy.template(map));
			} catch (Exception e) {
				log.error("记录mq发送端错误日志失败", e);
			}
		}
		//不管成功与否读删除redis里面备份的数据
		redisTemplate.delete(correlationData.getId());
	}

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		log.error("消息主体 message : " + message);
		log.error("描述：" + replyText);
		log.error("消息使用的交换器 exchange : " + exchange);
		log.error("消息使用的路由键 routing : " + routingKey);
	}

}
