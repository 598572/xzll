package com.xzll.test.cluster;

import com.xzll.common.rocketmq.ClusterEvent;
import com.xzll.common.rocketmq.RocketMQClusterEventListener;
import com.xzll.test.config.mq.RocketMqConsumerWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hzz
 * @Date: 2023/3/1 11:25:10
 * @Description: 某一批topic订阅与消费
 */
@Slf4j
@Component
public class ImRocketMQClusterEventBusConsumer implements RocketMQClusterEventListener, InitializingBean {

	public static final String XZLL_TEST_TOPIC = "xzll-test-topic";
	@Autowired
	private RocketMqConsumerWrap consumer;

	/**
	 * 初始化该类要监听的topic 并且调用RocketMqCustomConsumer的subscribe方法，进行订阅和启动consumer
	 *
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		List<String> topics = new ArrayList<>();
		topics.add(XZLL_TEST_TOPIC);
		consumer.subscribe(topics, this);
	}

	@Override
	public void handleEvent(String topicName, ClusterEvent clusterEvent) {

		if (XZLL_TEST_TOPIC.equals(topicName)) {
			String data = clusterEvent.getData();
			log.info("接收到的data数据:{}",data);
			log.info("进行业务逻辑消费topicName:{}", topicName);
		}
	}
}
