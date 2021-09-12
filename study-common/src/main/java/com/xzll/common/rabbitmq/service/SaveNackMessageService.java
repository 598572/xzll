package com.xzll.common.rabbitmq.service;

import com.xzll.common.rabbitmq.eneity.NackConsumerMessageDO;
import com.xzll.common.rabbitmq.eneity.NackProducerMessageDO;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/09/14 17:13
 * @Description:
 */
public interface SaveNackMessageService {

	void saveConsumerNackMessage(NackConsumerMessageDO nackConsumerMessageDO);
	void saveProducerNackMessage(NackProducerMessageDO nackProducerMessageDO);
}
