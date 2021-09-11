package com.xzll.common.rabbitmq.producer;

import com.xzll.common.rabbitmq.eneity.MqMessage;

/**
 * @Author: hzz
 * @Date: 2021/9/10 12:53:33
 * @Description:
 */
public interface ProducerService {

    /**
     * 发送消息
     * @param content
     * @param exchangeName
     * @param routingKey
     */
    void sendMsg(MqMessage content, String exchangeName, String routingKey);
}
