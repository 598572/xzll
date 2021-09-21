package com.xzll.common.email.config;

import java.util.List;

/**
 * @Author: hzz
 * @Date: 2021/9/18 16:33:34
 * @Description:
 * 假装 这是个redis模板对象
 * 我们需要把他交给spring,在使用redis时候
 * 我们直接调用该模板中的方法即可
 */
public class RedisTemplateExample {

	private String string;
	private String hash;
	private String set;
	private List<String> list;
	private String zset;

	/**
	 * 模拟 redis 的 list操作
	 * @param param
	 * @return
	 */
	public boolean lpush(String param){
		return list.add(param);
	}
}
