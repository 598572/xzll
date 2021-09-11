package com.xzll.test.rabbitmq;

import com.xzll.common.rabbitmq.eneity.MqMessage;
import lombok.Data;

/**
 * @Author: hzz
 * @Date: 2021/9/10 13:21:23
 * @Description:
 */
@Data
public class UserMQ extends MqMessage {
	private String name;
	private Integer sex;
	private String address;
	private Integer age;
}
