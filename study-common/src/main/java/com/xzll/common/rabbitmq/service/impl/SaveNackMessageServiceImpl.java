package com.xzll.common.rabbitmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.xzll.common.mapper.SaveProducerNackMessageMapper;
import com.xzll.common.rabbitmq.eneity.NackConsumerMessageDO;
import com.xzll.common.mapper.SaveConsumerNackMessageMapper;
import com.xzll.common.rabbitmq.eneity.NackProducerMessageDO;
import com.xzll.common.rabbitmq.service.SaveNackMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/09/14 17:13
 * @Description:
 */
@Slf4j
@Service
public class SaveNackMessageServiceImpl implements SaveNackMessageService {

	@Autowired
	private SaveConsumerNackMessageMapper saveConsumerNackMessageMapper;

	@Autowired
	private SaveProducerNackMessageMapper saveProducerNackMessageMapper;

	@Override
	public void saveProducerNackMessage(NackProducerMessageDO nackProducerMessageDO) {
		int insert = saveProducerNackMessageMapper.insert(nackProducerMessageDO);
		if (insert > 0) {
			log.info("保存生产者未投递成功的消息成功 : {}", JSON.toJSONString(nackProducerMessageDO));
			return;
		}
		log.info("保存生产者未投递成功的消息失败 : {}", JSON.toJSONString(nackProducerMessageDO));
	}


	@Override
	public void saveConsumerNackMessage(NackConsumerMessageDO nackConsumerMessageDO) {
		int insert = saveConsumerNackMessageMapper.insert(nackConsumerMessageDO);
		if (insert > 0) {
			log.info("保存消费者未消费消息成功 : {}", JSON.toJSONString(nackConsumerMessageDO));
			return;
		}
		log.info("保存消费者未消费消息失败 : {}", JSON.toJSONString(nackConsumerMessageDO));
	}
}
