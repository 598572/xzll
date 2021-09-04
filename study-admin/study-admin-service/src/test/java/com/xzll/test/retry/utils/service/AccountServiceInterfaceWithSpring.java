package com.xzll.test.retry.utils.service;

/**
 * @Author: hzz
 * @Date: 2021/9/3 18:29:43
 * @Description: 接口
 */
public interface AccountServiceInterfaceWithSpring {

	 String callAccountApi(String account);

	 String callAccountApiTwo(String account);
}
