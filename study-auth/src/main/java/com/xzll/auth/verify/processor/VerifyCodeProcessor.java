package com.xzll.auth.verify.processor;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 创建,校验验证码处理器
 */
public interface VerifyCodeProcessor {

	/**
	 * 创建验证码
	 *
	 * @param request 请求
	 * @throws Exception 异常
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * 验证验证码
	 *
	 * @param request 请求
	 */
	void validate(ServletWebRequest request);

}
