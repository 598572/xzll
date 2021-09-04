package com.xzll.test.retry.test;

import com.xzll.test.retry.proxy.JdkDynamicProxy;
import com.xzll.test.retry.service.classtest.AccountServiceSubClass;
import com.xzll.test.retry.service.interfacetest.AccountServiceImpl;
import com.xzll.test.retry.service.interfacetest.AccountServiceInterface;

/**
 * @Author: hzz
 * @Date: 2021/9/4 11:52:04
 * @Description: 动态代理演示
 */
public class JdkProxyTest {


	/**
	 * jdk代理演示
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//将代理对象的 class 文件写入到磁盘
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

		//1. 基于接口的代理
		AccountServiceInterface proxy = (AccountServiceInterface) JdkDynamicProxy.getProxy(new AccountServiceImpl(), AccountServiceInterface.class);
		System.out.println(proxy.callAccountApi("yes"));
		System.out.println("动态代理基于接口的演示 (jdk方式) ===== 可以正常执行");

		System.out.println();

		//2. 基于类的代理 将会强转失败
		AccountServiceSubClass proxy2 = (AccountServiceSubClass) JdkDynamicProxy.getProxy(new AccountServiceSubClass(), null);
		//AccountServiceClassParent proxy3 = (AccountServiceClassParent)DynamicProxy.getProxy(new AccountServiceSubClass(), null);  这行也会报错
		System.out.println(proxy2.callAccountApi("yes"));
		System.out.println("动态代理基于类的演示 (jdk方式) ===== 会报错");

		/*
		输出如下

		代理目标的接口是 : interface com.xzll.test.retry.service.interfacetest.AccountServiceInterface
		代理出来的对象是 : com.xzll.test.retry.service.interfacetest.AccountServiceImpl@5fdef03a
		调用账户信息接口成功
		动态代理基于接口的演示 (jdk方式) ===== 可以正常执行

		代理目标的接口是 : null
		代理出来的对象是 : com.xzll.test.retry.service.classtest.AccountServiceSubClass@6a6824be
		Exception in thread "main" java.lang.ClassCastException: com.sun.proxy.$Proxy1 cannot be cast to com.xzll.test.retry.service.classtest.AccountServiceSubClass
			at com.xzll.test.retry.Hzz.main(Hzz.java:25)

		 */
	}

}
