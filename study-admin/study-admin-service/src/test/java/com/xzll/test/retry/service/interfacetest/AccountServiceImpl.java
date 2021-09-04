package com.xzll.test.retry.service.interfacetest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: hzz
 * @Date: 2021/9/3 18:29:43
 * @Description: 实现
 */
@Service
public class AccountServiceImpl implements AccountServiceInterface{


	@Override
	public String callAccountApi(String account) {
		if (StringUtils.isNoneBlank(account)) {
			return "调用账户信息接口成功";
		}
		throw new NullPointerException();
	}

	@Override
	public String callAccountApiTwo(String account) {
		if (StringUtils.isNoneBlank(account)) {
			return "调用账户信息接口成功";
		}
		throw new NullPointerException();
	}
}
