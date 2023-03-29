package com.xzll.common.http;


/**
 * @Author: hzz
 * @Date: 2023/2/28 12:01:30
 * @Description: 请求类型枚举
 */
public enum HttpRequestContentType {

	FORM("FORM", "application/x-www-form-urlencoded"),
	JSON("JSON", "application/json"),
	MULTIPART("MULTIPART", "multipart/form-data");

	HttpRequestContentType(String type, String desc){
		this.type = type;
		this.desc = desc;
	}

	private String type;

	private String desc;


	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
