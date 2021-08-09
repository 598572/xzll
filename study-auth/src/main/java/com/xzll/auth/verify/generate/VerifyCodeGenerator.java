package com.xzll.auth.verify.generate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:15
 * @Description: 验证码生成器接口  替换之前util的写法  这次咱们面向对象编程 哈哈
 */
public interface VerifyCodeGenerator {
	/**
	 * 生成验证码
	 *
	 * @param request 请求
	 * @return 生成结果
	 */
	String generate(ServletWebRequest request);
}
