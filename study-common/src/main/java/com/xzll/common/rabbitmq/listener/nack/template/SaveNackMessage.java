package com.xzll.common.rabbitmq.listener.nack.template;

import com.xzll.common.rabbitmq.listener.nack.template.impl.SaveConsumerNackMessage;
import com.xzll.common.rabbitmq.listener.nack.template.impl.SaveProducerNackMessage;
import com.xzll.common.rabbitmq.service.SaveNackMessageService;
import com.xzll.common.util.SpringUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/12 13:22:30
 * @Description: 对未ack的消息进行持久化操作 策略+模板方法模式实现
 */
public abstract class SaveNackMessage implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SaveNackMessage.class);
	protected static SaveNackMessageService saveNackMessageService;
	static {
		//获取保存数据的Service 由于SaveNackMessage不被spring管理 所以直接@Autoware的话，属性将会为null ，故此处采用静态块来加载所需的Service
		SaveNackMessageService nackMessageService = (SaveNackMessageService) SpringUtil.getBean(SaveNackMessageService.class);
		if (nackMessageService != null) {
			saveNackMessageService = nackMessageService;
		} else {
			logger.error("获取bean nackMessageService 失败 : {}", nackMessageService);
		}
	}

	private static Map<Integer, SaveNackMessage> strategy = new HashMap<>();

	static {
		//初始化策略对象
		strategy.put(NackTypeEnum.PRODUCER.getType(), new SaveProducerNackMessage());
		strategy.put(NackTypeEnum.CONSUMER.getType(), new SaveConsumerNackMessage());
	}
	//当前获取到的策略实现 这里把他提出来是为了在模板方法中返回用
	private static SaveNackMessage saveNackMessage;

	/**
	 * 获取策略根据type
	 *
	 * @param type
	 * @return
	 */
	public static SaveNackMessage getStrategy(Integer type) {
		saveNackMessage = strategy.get(type);
		return saveNackMessage;
	}

	//填充数据 因为consumer和producer的数据格式不一致 所以需要子类去构造数据格式
	protected abstract void fillData(Object object);

	//模板方法 定义步骤流程
	public final SaveNackMessage template(Object object) {
		//填充数据
		this.fillData(object);
		//返回Runnable用于线程池执行run方法
		return saveNackMessage;
	}


	//枚举类 用于 区分不同场景的nack数据
	public enum NackTypeEnum {
		PRODUCER(1, "保存生产者未投递成功的消息"),
		CONSUMER(2, "保存消费者者未消费的消息"),
		DLX_NACK(3, "保存死信队列中未消费成功的消息");//TODO 待实现

		@Getter
		private Integer type;
		@Getter
		private String desc;

		NackTypeEnum(Integer type, String desc) {
			this.type = type;
			this.desc = desc;
		}

	}

}
