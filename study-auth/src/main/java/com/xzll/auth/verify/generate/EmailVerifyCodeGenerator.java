package com.xzll.auth.verify.generate;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:17
 * @Description: 邮件验证码生成器
 */
@Component
public class EmailVerifyCodeGenerator implements VerifyCodeGenerator {

	@Override
	public String generate(ServletWebRequest request) {
		// 定义手机验证码生成策略，可以使用 request 中从请求动态获取生成策略
		// 可以从配置文件中读取生成策略
		return RandomUtil.randomNumbers(6);
	}

}
