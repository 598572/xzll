package com.xzll.auth.verify.holder;

import com.xzll.auth.verify.processor.VerifyCodeProcessor;
import com.xzll.common.base.XzllBaseException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:38
 * @Description: 验证码处理器的持有者
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeProcessorHolder {


	@Autowired
	private  Map<String, VerifyCodeProcessor> validateCodeProcessorMap;

	/**
	 * 通过验证码类型查找
	 *
	 * @param type 验证码类型
	 * @return 验证码处理器
	 */
	public VerifyCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + VerifyCodeProcessor.class.getSimpleName();
		VerifyCodeProcessor processor = validateCodeProcessorMap.get(name);
		if (Objects.isNull(processor)) {
			throw new XzllBaseException("验证码处理器" + name + "不存在");
		}
		return processor;
	}
}
