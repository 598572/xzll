package com.xzll.test.retry.utils;

import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.retry.utils.service.AccountServiceInterfaceWithSpring;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: hzz
 * @Date: 2021/9/4 19:41:31
 * @Description: 测试Retryable注解的效果
 */

public class RetryUtilsTest extends StudyTestApplicationTest {

	@Autowired
	private AccountServiceInterfaceWithSpring accountServiceInterfaceWithSpring;

	@Test
	public void queryAccountProxyClass() {

		String noRetry = accountServiceInterfaceWithSpring.callAccountApi("正常情况");
		System.out.println("noRetry: " + noRetry);

		String doRetry = accountServiceInterfaceWithSpring.callAccountApi(null);
		System.out.println("doRetry: " + doRetry);

	}


}
