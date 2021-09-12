package com.xzll.common.rabbitmq.listener.nack.template.impl;

import com.alibaba.fastjson.JSON;
import com.xzll.common.rabbitmq.eneity.NackProducerMessageDO;
import com.xzll.common.rabbitmq.listener.nack.template.SaveNackMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: hzz
 * @Date: 2021/9/12 13:22:30
 * @Description: 对未ack的消息进行持久化操作 针对生产者未ack的情况
 */
public class SaveProducerNackMessage extends SaveNackMessage implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SaveNackMessage.class);

	private NackProducerMessageDO nackProducerMessageDO;

	@Override
	public void fillData(Object data) {
		this.nackProducerMessageDO = new NackProducerMessageDO();
		Map<String, Object> map = (Map<String, Object>) data;
		if (Objects.nonNull(map.get("ack"))) {
			this.nackProducerMessageDO.setAck((Integer) map.get("ack"));
		}
		if (Objects.nonNull(map.get("correlationData"))) {
			this.nackProducerMessageDO.setCorrelationData(JSON.toJSONString(map.get("correlationData")));
		}
		if (Objects.nonNull(map.get("cause"))) {
			this.nackProducerMessageDO.setCause(JSON.toJSONString(map.get("cause")));
		}
		this.nackProducerMessageDO.setCtime(new Date());
		this.nackProducerMessageDO.setUtime(new Date());
	}

	/**
	 * 保存未消费的消息到db
	 */
	@Override
	public void run() {
		super.saveNackMessageService.saveProducerNackMessage(this.nackProducerMessageDO);
		logger.info("进行持久化操作");
	}
}
