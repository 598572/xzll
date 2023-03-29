package com.xzll.order.mq;


import com.xzll.common.rocketmq.RocketMqConsumerMessageHook;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: hzz
 * @Date: 2021/9/3 12:47:09
 * @Description:
 */
@Component
public class RocketMqProducerAndConsumerConfig {





	//--------------consumer





//	@Value("${rocketmq.consumer.namesrvAddr}")
//	private String namesrvAddr;
//	@Value("${rocketmq.consumer.groupName}")
//	private String groupName;
//	@Value("${rocketmq.consumer.consumeThreadMin}")
//	private int consumeThreadMin;
//	@Value("${rocketmq.consumer.consumeThreadMax}")
//	private int consumeThreadMax;
//	@Value("${rocketmq.consumer.topics}")
//	private String topics;
//	@Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
//	private int consumeMessageBatchMaxSize;


	@Bean
	public DefaultMQPushConsumer defaultMQPushConsumer() {
//		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
//		consumer.setNamesrvAddr(namesrvAddr);
//		consumer.setConsumeThreadMin(consumeThreadMin);
//		consumer.setConsumeThreadMax(consumeThreadMax);
//		consumer.registerMessageListener(mqMsgListener);
//		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//		// consumer.setMessageModel(MessageModel.CLUSTERING);
//		consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
//		try {
//			consumer.subscribe(topics, "MyTag");
//			consumer.start();
//		} catch (MQClientException e) {
//		}

		// 实例化消费者 ，启动和订阅不在这里，是在有业务方订阅时候，被动触发 订阅和启动消费者逻辑
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("OrderConsumer");
		consumer.setNamesrvAddr("127.0.0.1:9876");
		consumer.getDefaultMQPushConsumerImpl().registerConsumeMessageHook(new RocketMqConsumerMessageHook());
		return consumer;
	}

}
