package com.xzll.common.rabbitmq.listener.nack.template.impl;

import com.alibaba.fastjson.JSON;
import com.xzll.common.rabbitmq.eneity.NackConsumerMessageDO;
import com.xzll.common.rabbitmq.listener.nack.template.SaveNackMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import java.util.Date;

/**
 * @Author: hzz
 * @Date: 2021/9/12 13:22:30
 * @Description: 对未ack的消息进行持久化操作 针对消费者未ack的情况
 */
public class SaveConsumerNackMessage extends SaveNackMessage implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SaveNackMessage.class);

	private NackConsumerMessageDO nackConsumerMessageDO;

	@Override
	public void fillData(Object data) {
		this.nackConsumerMessageDO = new NackConsumerMessageDO();
		Message message=(Message) data;
		this.nackConsumerMessageDO.setMessage(JSON.toJSONString(message));
		this.nackConsumerMessageDO.setCtime(new Date());
		this.nackConsumerMessageDO.setUtime(new Date());
	}

	/**
	 * 保存未消费的消息到db
	 */
	@Override
	public void run() {
		super.saveNackMessageService.saveConsumerNackMessage(this.nackConsumerMessageDO);
		logger.info("进行持久化操作");
	}
}
