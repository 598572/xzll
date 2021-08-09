package com.xzll.auth.controller;

import com.xzll.auth.verify.holder.VerifyCodeProcessorHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@RestController
@RequestMapping("/verify")
public class VerifyCodeController {

	@Autowired
	private VerifyCodeProcessorHolder verifyCodeProcessorHolder;

	/**
	 * 通过 type 进行查询到对应的处理器
	 * 同时创建验证码
	 *
	 * @param request  请求
	 * @param response 响应
	 * @param type     验证码类型
	 * @throws Exception 异常
	 */
	@GetMapping("/code/{type}")
	public void createCode(HttpServletRequest request, HttpServletResponse response,
						   @PathVariable String type) throws Exception {
		verifyCodeProcessorHolder.findValidateCodeProcessor(type)
				.create(new ServletWebRequest(request, response));
	}

}
