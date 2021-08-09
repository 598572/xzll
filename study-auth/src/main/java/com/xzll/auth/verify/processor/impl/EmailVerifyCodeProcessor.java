package com.xzll.auth.verify.processor.impl;

import com.xzll.auth.verify.processor.AbstractVerifyCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 16:58
 * @Description:
 */
@Component
public class EmailVerifyCodeProcessor extends AbstractVerifyCodeProcessor {

	@Override
	protected void send(ServletWebRequest request, String verifyCode) {

		System.out.println(request.getHeader("email") +
				"邮箱验证码发送成功，验证码为：" + verifyCode);
	}

}
