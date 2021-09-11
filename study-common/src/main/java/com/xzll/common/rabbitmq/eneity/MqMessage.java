package com.xzll.common.rabbitmq.eneity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author: hzz
 * @Date: 2021/9/10 12:59:33
 * @Description:
 */
@Data
public class MqMessage implements Serializable {

    /**
     * 消息ID
     */
    protected String id = UUID.randomUUID().toString();
    /**
     * 单个消息过期时间
     */
    protected String expiration;
}
