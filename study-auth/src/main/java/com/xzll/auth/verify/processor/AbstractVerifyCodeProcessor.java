package com.xzll.auth.verify.processor;

import com.xzll.auth.verify.generate.VerifyCodeGenerator;
import com.xzll.common.base.XzllBaseException;
import com.xzll.common.util.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.Objects;

import static com.xzll.common.base.constant.RedisConstantKey.AuthConstantRedisKey.phoneVerifyKey;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 16:54
 * @Description: 模板方法实现抽象策略 目的，将这几种策略的公共内容 (如保存至redis,发送信息等操作抽取出来成为公用的)
 */
@Slf4j
public abstract class AbstractVerifyCodeProcessor implements VerifyCodeProcessor {


	@Autowired
	private RedisClient redisClient;

	/**
	 * 该行为会将所有实现了VerifyCodeGenerator接口的bean 放入map中去 key为bean的名称 value为具体的bean对象 666 策略模式的好伙伴
	 */
	@Autowired
	private Map<String, VerifyCodeGenerator> verifyCodeGeneratorMap;

	@Override
	public void create(ServletWebRequest request) throws Exception {
		String validateCode = generate(request);
		//保存并发送
		save(request, validateCode,getValidateCodeType(request));
		send(request, validateCode);
	}

	@Override
	public void validate(ServletWebRequest request) {

	}

	/**
	 * 发送验证码，由子类实现 (hook函数)
	 *
	 * @param request      请求
	 * @param validateCode 验证码
	 */
	protected abstract void send(ServletWebRequest request, String validateCode);

	/**
	 * 保存验证码，保存到 redis 中
	 *
	 * @param request 请求
	 * @param code    验证码
	 */
	private void save(ServletWebRequest request, String code, String type) {
		String header = request.getHeader(type);
		boolean set = redisClient.set(phoneVerifyKey(header, type), code, 120);
		if (set) {
			log.info("保存验证码成功 code:{},type:{}", code, type);
		}
	}



	/**
	 * 生成验证码
	 *
	 * @param request 请求
	 * @return 验证码
	 */
	private String generate(ServletWebRequest request) {
		String type = getValidateCodeType(request);
		String componentName = type + VerifyCodeGenerator.class.getSimpleName();
		VerifyCodeGenerator generator = verifyCodeGeneratorMap.get(componentName);
		if (Objects.isNull(generator)) {
			throw new XzllBaseException("验证码生成器 " + componentName + " 不存在。");
		}
		return generator.generate(request);
	}

	/**
	 * 根据请求 url 获取验证码类型
	 *
	 * @return 结果
	 */
	private String getValidateCodeType(ServletWebRequest request) {
		String uri = request.getRequest().getRequestURI();
		int index = uri.lastIndexOf("/") + 1;
		return uri.substring(index).toLowerCase();
	}
}
