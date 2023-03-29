package com.xzll.test.entity;

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
	private String desc;
	private String ctime;

	public UserMQ(String name) {
		this.name = name;
	}

	public UserMQ() {
	}

	public UserMQ(String name, Integer sex, String address, Integer age, String desc, String ctime) {
		this.name = name;
		this.sex = sex;
		this.address = address;
		this.age = age;
		this.desc = desc;
		this.ctime = ctime;
	}
}
