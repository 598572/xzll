package com.xzll.test.retry.service.classtest;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: hzz
 * @Date: 2021/9/3 18:29:43
 * @Description:
 */
public class AccountServiceParentClass {

	public String callAccountApi(String account) {
		if (StringUtils.isNoneBlank(account)) {
			return "调用账户信息接口成功";
		}
		return "调用账户信息接口失败";
	}
}
