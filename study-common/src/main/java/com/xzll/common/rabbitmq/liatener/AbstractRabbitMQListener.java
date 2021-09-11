package com.xzll.common.rabbitmq.liatener;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xzll.common.rabbitmq.exception.MqException;
import com.xzll.common.rabbitmq.eneity.MqMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hzz
 * @Date: 2021/9/10 13:59:49
 * @Description: 抽象MQ监听器，通用的处理流程(即接收消息以及失败处理)都已经包含，子类可通过钩子函数来进行具体的处理逻辑
 */
public abstract class AbstractRabbitMQListener<T extends MqMessage> {

	//日志
	protected static Logger logger = LoggerFactory.getLogger(AbstractRabbitMQListener.class);
	//消息序列化类型 默认UTF-8
	private static final SerializerMessageConverter SERIALIZER_MESSAGE_CONVERTER = new SerializerMessageConverter();
	//编码类型 也是 UTF-8
	private static final String ENCODING = Charset.defaultCharset().name();
	//消息体父类
	public static final String PARENT_MESSAGE_CLASS = "com.xzll.common.rabbitmq.eneity.MqMessage";

	//注入redis  用于消息幂等，防止重复消费
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 正常业务执行  钩子方法 用于子类去拓展
	 *
	 * @param content
	 */
	protected abstract void successExecuteHook(T content) throws Exception;

	/**
	 * 失败执行 钩子方法 用于子类去拓展
	 *
	 * @param content
	 */
	protected abstract void failExecuteHook(T content);


	//--------------------------------------------一以下为模板方法，用于接收，幂等，ack, nack等---------------------------------------------

	/**
	 * 处理接收到的消息,   模板方法 子类一般无需扩展 ， 调用方式: super.receiveMessage(message,channel);
	 *
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	protected void receiveMessage(Message message, Channel channel) throws Exception {
		/**
		 * 做数据幂等校验，可以根据传过来的唯一ID先判断缓存 or 数据库中是否有数据（这里使用redis）
		 * 1、有数据则不消费，直接响应ack
		 * 2、缓存没有数据，则进行消费处理数据，处理完后手动ack
		 * 3、如果消息处理异常则，可以存入数据库中(或者存入死信队列)，（另外可以增加短信、邮件提醒，钉钉消息等功能）
		 */
		try {
			T content = getContent(message);
			//已经消费，直接返回
			if (checkConsumedFlag(content, message.getMessageProperties().getConsumerQueue())) {
				logger.info(message.getMessageProperties().getConsumerQueue() + "已经消费过");
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} else {
				//未消费过的话，消费当前消息
				successExecuteHook(content);
				logger.info(message.getMessageProperties().getConsumerQueue() + "消费成功");
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

				//消费成功后删除重试标志
				String redisCountKey = "retry:" + message.getMessageProperties().getConsumerQueue() + content.getId();
				redisTemplate.delete(redisCountKey);
			}
		} catch (Exception e) {
			//在catch中手动处理异常，当有异常且不手动处理的话，rabbitmq将会重试，重试参数配置见本类的最下边。
			e.printStackTrace();
			try {
				if (dealFailAck(message, channel)) {
					logger.info("回归队列成功：" + message);
				} else {
					logger.error("回归队列失败：" + message);
					//TODO 进行持久化 异步 使用线程池的方式 明天补上
					failExecuteHook(getContent(message));
				}
			} catch (Exception e1) {
				//扔掉数据
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				logger.error("重试消费失败：" + message);
				failExecuteHook(getContent(message));
			}
		}
	}

	/**
	 * 失败ACK
	 *
	 * @param message
	 * @param channel
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private Boolean dealFailAck(Message message, Channel channel) throws IOException, InterruptedException {
		T content = getContent(message);
		//单个消息控制
		String redisCountKey = "retry:" + message.getMessageProperties().getConsumerQueue() + content.getId();
		String retryCountValue = redisTemplate.opsForValue().get(redisCountKey);
		long deliveryTag = message.getMessageProperties().getDeliveryTag();

		//若该条消息没有重试消费过
		if (StringUtils.isBlank(retryCountValue)) {
			//第一次重试时候  设置重试次数
			redisTemplate.opsForValue().setIfAbsent(redisCountKey, "1", 5, TimeUnit.MINUTES);
				/*
				basicNack参数解释:

					deliveryTag:该消息的index
					multiple：是否批量nack ， true:将拒绝确认所有小于deliveryTag的消息。
					requeue：被拒绝的是否重新入队列，false的话是直接丢弃，true是回归到队列中。
				*/
			logger.info(" {} 开始第一次回归到队列：", deliveryTag);
			channel.basicNack(deliveryTag, false, true);
			return true;
		} else {
			switch (Integer.valueOf(retryCountValue)) {
				case 1:
					redisTemplate.opsForValue().set(redisCountKey, "2");
					logger.info(" {} 开始第二次回归到队列：", deliveryTag);
					channel.basicNack(deliveryTag, false, true);
					return true;
				case 2:
					redisTemplate.opsForValue().set(redisCountKey, "3");
					logger.info(" {} 开始第三次回归到队列：", deliveryTag);
					channel.basicNack(deliveryTag, false, true);
					return true;
				case 3:
					redisTemplate.opsForValue().set(redisCountKey, "4");
					logger.info(" {} 开始第四次回归到队列：", deliveryTag);
					channel.basicNack(deliveryTag, false, true);
					return true;
				default:
					//扔掉消息，放入死信队列或者存入数据库
					redisTemplate.delete(redisCountKey);
					//b1=false的话，代表丢弃消息(如果配置了死信队列的话，该消息不会被丢弃 而是进入死信队列中)
					channel.basicNack(deliveryTag, false, false);
					logger.info(" {} 不回归队列，进行持久化处理或者放入死信队列中：", deliveryTag);
					//TODO 异步 写入数据库

					return false;
			}
		}
		//实际上最好在这里进行Nack 但是为了看起来清晰些  我把Nack写到上边每一个需要响应Nack的地方了。
	}

	/**
	 * @param message
	 * @return
	 */
	private T getContent(Message message) {
		String body = getBodyContentAsString(message);
		Class<T> contentClass = null;
		try {
			contentClass = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		} catch (Exception e) {
			throw new MqException("缺失泛型");
		}
		if (contentClass != null && contentClass.getName().equals(PARENT_MESSAGE_CLASS)) {
			throw new MqException("请指定相应的消息类型");
		}
		T content = JSONObject.toJavaObject(JSONObject.parseObject(body), contentClass);
		return content;
	}

	/**
	 * 获取message的body
	 *
	 * @param message
	 * @return
	 */
	private String getBodyContentAsString(Message message) {
		if (message.getBody() == null) {
			return null;
		}
		try {
			String contentType = (message.getMessageProperties() != null) ? message.getMessageProperties().getContentType() : null;
			if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
				return SERIALIZER_MESSAGE_CONVERTER.fromMessage(message).toString();
			}
			if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType)
					|| MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
				return new String(message.getBody(), ENCODING);
			}
		} catch (Exception e) {
			// ignore
		}
		// Comes out as '[B@....b' (so harmless)
		return message.getBody().toString() + "(byte[" + message.getBody().length + "])";
	}

	/**
	 * 是否能消费，用于防止重复消费
	 * <p>
	 * false 代表未消费过 ，true代表消费过
	 *
	 * @param content
	 * @param queueName
	 * @return
	 */
	private Boolean checkConsumedFlag(T content, String queueName) {
		String messageKey = queueName + ":" + content.getId();
		if (StringUtils.isBlank(redisTemplate.opsForValue().get(messageKey))) {
			//从redis中没获取到value，说明未消费过该消息，返回true
			return false;
		} else {
			//获取到了value说明消费过，然后将该消息标记为已消费并直接响应ack，不进行下边的业务处理，防止消费n次(保证幂等)
			redisTemplate.opsForValue().set(messageKey, "lock", 60, TimeUnit.SECONDS);
			return true;
		}
	}

	/*

	TODO *** 关于rabbitmq的配置参数详解  重要 ***



	基础信息
	spring.rabbitmq.host: 默认localhost
	spring.rabbitmq.port: 默认5672
	spring.rabbitmq.username: 用户名
	spring.rabbitmq.password: 密码
	spring.rabbitmq.virtual-host: 连接到代理时用的虚拟主机
	spring.rabbitmq.addresses: 连接到server的地址列表（以逗号分隔），先addresses后host
	spring.rabbitmq.requested-heartbeat: 请求心跳超时时间，0为不指定，如果不指定时间单位默认为妙
	spring.rabbitmq.publisher-confirms: 是否启用【发布确认】，默认false
	spring.rabbitmq.publisher-returns: 是否启用【发布返回】，默认false
	spring.rabbitmq.connection-timeout: 连接超时时间，单位毫秒，0表示永不超时

	SSL
	spring.rabbitmq.ssl.enabled: 是否支持ssl，默认false
	spring.rabbitmq.ssl.key-store: 持有SSL certificate的key store的路径
	spring.rabbitmq.ssl.key-store-password: 访问key store的密码
	spring.rabbitmq.ssl.trust-store: 持有SSL certificates的Trust store
	spring.rabbitmq.ssl.trust-store-password: 访问trust store的密码
	spring.rabbitmq.ssl.trust-store-type=JKS：Trust store 类型.
	spring.rabbitmq.ssl.algorithm: ssl使用的算法，默认由rabiitClient配置
	spring.rabbitmq.ssl.validate-server-certificate=true：是否启用服务端证书验证
	spring.rabbitmq.ssl.verify-hostname=true 是否启用主机验证

	缓存cache
	spring.rabbitmq.cache.channel.size: 缓存中保持的channel数量
	spring.rabbitmq.cache.channel.checkout-timeout: 当缓存数量被设置时，从缓存中获取一个channel的超时时间，单位毫秒；如果为0，则总是创建一个新channel
	spring.rabbitmq.cache.connection.size: 缓存的channel数，只有是CONNECTION模式时生效
	spring.rabbitmq.cache.connection.mode=channel: 连接工厂缓存模式：channel 和 connection

	Listener
	spring.rabbitmq.listener.type=simple: 容器类型.simple或direct
	spring.rabbitmq.listener.simple.auto-startup=true: 是否启动时自动启动容器
	spring.rabbitmq.listener.simple.acknowledge-mode: 表示消息确认方式，其有三种配置方式，分别是none、manual和auto；默认auto
	spring.rabbitmq.listener.simple.concurrency: 最小的消费者数量
	spring.rabbitmq.listener.simple.max-concurrency: 最大的消费者数量
	spring.rabbitmq.listener.simple.prefetch: 一个消费者最多可处理的nack消息数量，如果有事务的话，必须大于等于transaction数量.
	spring.rabbitmq.listener.simple.transaction-size: 当ack模式为auto时，一个事务（ack间）处理的消息数量，最好是小于等于prefetch的数量.若大于prefetch， 则prefetch将增加到这个值
	spring.rabbitmq.listener.simple.default-requeue-rejected: 决定被拒绝的消息是否重新入队；默认是true（与参数acknowledge-mode有关系）
	spring.rabbitmq.listener.simple.missing-queues-fatal=true 若容器声明的队列在代理上不可用，是否失败； 或者运行时一个多多个队列被删除，是否停止容器
	spring.rabbitmq.listener.simple.idle-event-interval: 发布空闲容器的时间间隔，单位毫秒
	spring.rabbitmq.listener.simple.retry.enabled=false: 是否开启重试
	spring.rabbitmq.listener.simple.retry.max-attempts=3: 最大重试次数
	spring.rabbitmq.listener.simple.retry.max-interval=10000ms: 最大重试时间间隔
	spring.rabbitmq.listener.simple.retry.initial-interval=1000ms:第一次和第二次尝试传递消息的时间间隔
	spring.rabbitmq.listener.simple.retry.multiplier=1: 应用于上一重试间隔的乘数
	spring.rabbitmq.listener.simple.retry.stateless=true: 重试时有状态or无状态
	spring.rabbitmq.listener.direct.acknowledge-mode= ack模式
	spring.rabbitmq.listener.direct.auto-startup=true 是否在启动时自动启动容器
	spring.rabbitmq.listener.direct.consumers-per-queue= 每个队列消费者数量.
	spring.rabbitmq.listener.direct.default-requeue-rejected= 默认是否将拒绝传送的消息重新入队.
	spring.rabbitmq.listener.direct.idle-event-interval= 空闲容器事件发布时间间隔.
	spring.rabbitmq.listener.direct.missing-queues-fatal=false若容器声明的队列在代理上不可用，是否失败.
	spring.rabbitmq.listener.direct.prefetch= 每个消费者可最大处理的nack消息数量.
	spring.rabbitmq.listener.direct.retry.enabled=false  是否启用发布重试机制.
	spring.rabbitmq.listener.direct.retry.initial-interval=1000ms # Duration between the first and second attempt to deliver a message.
	spring.rabbitmq.listener.direct.retry.max-attempts=3 # Maximum number of attempts to deliver a message.
	spring.rabbitmq.listener.direct.retry.max-interval=10000ms # Maximum duration between attempts.
	spring.rabbitmq.listener.direct.retry.multiplier=1 # Multiplier to apply to the previous retry interval.
	spring.rabbitmq.listener.direct.retry.stateless=true # Whether retries are stateless or stateful.

	Template
	spring.rabbitmq.template.mandatory: 启用强制信息；默认false
	spring.rabbitmq.template.receive-timeout: receive() 操作的超时时间
	spring.rabbitmq.template.reply-timeout: sendAndReceive() 操作的超时时间
	spring.rabbitmq.template.retry.enabled=false: 发送重试是否可用
	spring.rabbitmq.template.retry.max-attempts=3: 最大重试次数
	spring.rabbitmq.template.retry.initial-interva=1000msl: 第一次和第二次尝试发布或传递消息之间的间隔
	spring.rabbitmq.template.retry.multiplier=1: 应用于上一重试间隔的乘数
	spring.rabbitmq.template.retry.max-interval=10000: 最大重试时间间隔


	 */
}
