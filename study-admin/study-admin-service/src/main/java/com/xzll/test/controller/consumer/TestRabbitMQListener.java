package com.xzll.test.controller.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xzll.common.rabbitmq.AbstractRabbitMQListener;
import com.xzll.test.entity.TestMqConstant;
import com.xzll.test.entity.UserMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: hzz
 * @Date: 2021/9/10 17:43:52
 * @Description: 测试RabbitMQ 实际场景的话，可以是一个业务定义一个交换机，比如名片业务，订单业务等等
 */
@Slf4j
@Component
public class TestRabbitMQListener extends AbstractRabbitMQListener<UserMQ> {


	//---------------------------------定义某业务 对应的 交换机 队列 routingKey ---------------------------------

	/**
	 * 1. 创建交换机 直连型
	 *
	 * @return
	 */
	@Bean
	public DirectExchange testDirectExchange() {
		DirectExchange directExchange = new DirectExchange(TestMqConstant.TEST_ZHILIAN_EX);
		return directExchange;
	}

	/**
	 * 2. 创建队列
	 * <p>
	 * 队列的三个属性:
	 * <p>
	 * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当rabbitmq重启时仍然存在，暂存队列：当前连接有效
	 * <p>
	 * exclusive:是否设置为排他队列，默认是false，如果是true的话只能被首次声明他的的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
	 * <p>
	 * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
	 *
	 * @return
	 */
	@Bean
	public Queue testQueue() {

		Map<String, Object> args = new HashMap<>(2);
		// x-dead-letter-exchange 这里声明当前队列绑定的死信交换机 （队列添加了这个参数之后会自动与该死信交换机绑定，并设置路由键，不需要开发者手动绑定)
		args.put("x-dead-letter-exchange", TestMqConstant.TEST_DEAD_LETTER_EXCHANGE);
		// x-dead-letter-routing-key 这里声明当前队列的死信路由key
		args.put("x-dead-letter-routing-key", TestMqConstant.TEST_DEAD_LETTER_ROUTING_KEY);


		//QueueBuilder不调用某方法的话，那么那个值就是false,调用了就是true
		//这里的exclusive和autoDelete都设置为false
		return QueueBuilder.durable(TestMqConstant.TEST_ZHILIAN_Q)
				.withArguments(args)
				.build();
		//之前写法 Queue queue = new Queue(TestMqConstant.TEST_ZHILIAN_Q, true, false, false);
	}

	/**
	 * 3. 绑定队列到交换机 并指定routingKey
	 *
	 * @return
	 */
	@Bean
	public Binding bindingTestDirectQueue() {
		return BindingBuilder
				.bind(testQueue())
				.to(testDirectExchange())
				.with(TestMqConstant.TEST_ZHILIAN_RK);
	}

	//-----------------------------------------该业务对应的死信队列信息--------------------------------------------

	/**
	 * 声明 死信交换机
	 *
	 * @return
	 */
	@Bean
	public DirectExchange testDeadLetterExchange() {
		DirectExchange directExchange = new DirectExchange(TestMqConstant.TEST_DEAD_LETTER_EXCHANGE);
		return directExchange;
	}

	/**
	 * 声明死信队列， 用这个队列来接收死信交换机投递过来的消息
	 *
	 * @return
	 */
	@Bean
	public Queue testDeadLetterQueue() {
		Queue queue = new Queue(TestMqConstant.TEST_DEAD_LETTER_QUEUE, true, false, false);
		return queue;
	}

	/**
	 * 绑定死信队列到死信交换机
	 *
	 * @return
	 */
	@Bean
	public Binding testDeadLetterBinding() {
		return BindingBuilder
				.bind(testDeadLetterQueue())
				.to(testDeadLetterExchange())
				.with(TestMqConstant.TEST_DEAD_LETTER_ROUTING_KEY);
	}

	//----------------------------- 业务消息监听 -----------------------------------------------

	/**
	 * 监听 某个queue 支持多个
	 * queuesToDeclare不存在的话创建 queue
	 *
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	@Override
	@RabbitListener(queues = TestMqConstant.TEST_ZHILIAN_Q)
	protected void receiveMessage(Message message, Channel channel) throws Exception {
		super.receiveMessage(message, channel);
	}

	//----------------------------- 死信消息监听 后期将封装到抽象父类中 -----------------------------

	@RabbitListener(queues = TestMqConstant.TEST_DEAD_LETTER_QUEUE)
	public void receiveDeadLetterMessage(Message message, Channel channel) {
		log.info("接收到死信消息:[{}]", JSON.toJSONString(message));
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			log.info("死信队列签收消息....消息路由键为:[{}]", message.getMessageProperties().getReceivedRoutingKey());
		} catch (IOException e) {
			log.error("死信队列消息签收失败", e);
		}
	}


	//-----------------------------处理具体的业务逻辑---------------------------------------------
	/**
	 * 该方法被 receiveMessage调用。 用于执行 正常逻辑
	 *
	 * @param content
	 * @throws Exception
	 */
	@Override
	protected void successExecuteHook(UserMQ content) throws Exception {
		log.info("开始消费消息，{}", JSONObject.toJSONString(content));
		log.info("性别除以年龄 如果年龄为0则会抛出异常，用于检测消费失败时候的nack逻辑 sex:{} , age:{}", content.getSex(), content.getAge());
		int i = content.getSex() / content.getAge();
		logger.info("性别除以年龄得出结果为: {}", i);
	}

	//-----------------------------失败后的业务逻辑---------------------------------------------
	/**
	 * 该方法被 receiveMessage调用。 用于执行 异常逻辑
	 *
	 * @param content
	 */
	@Override
	protected void failExecuteHook(UserMQ content) {
		log.error("消费数据出错：{}", JSONObject.toJSONString(content));
	}
}
