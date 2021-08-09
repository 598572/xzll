package com.xzll.auth.verify.processor.impl;

import com.xzll.auth.verify.processor.AbstractVerifyCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 16:57
 * @Description:
 */
@Component
public class PhoneVerifyCodeProcessor extends AbstractVerifyCodeProcessor {
	@Override
	protected void send(ServletWebRequest request, String verifyCode) {
		System.out.println(request.getHeader("phone") +
				"手机验证码发送成功，验证码为：" + verifyCode);
	}

}
