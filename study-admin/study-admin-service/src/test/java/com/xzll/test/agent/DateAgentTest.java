package com.xzll.test.agent;

import java.util.Date;

/**
 * @Author: hzz
 * @Date: 2023/3/3 09:15:21
 * @Description:
 */
public class DateAgentTest {

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println("使用agent插桩技术使java.util.Date类的getTime()方法返回的时间戳为秒级别，当前时间戳【秒级】："
				+date.getTime()+"");
	}
}
