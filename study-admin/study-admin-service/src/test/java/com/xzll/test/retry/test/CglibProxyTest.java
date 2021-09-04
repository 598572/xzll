package com.xzll.test.retry.test;

import com.xzll.test.retry.proxy.CglibDynamicProxy;
import com.xzll.test.retry.service.classtest.AccountServiceParentClass;
import com.xzll.test.retry.service.classtest.AccountServiceSubClass;
import com.xzll.test.retry.service.interfacetest.AccountServiceImpl;
import com.xzll.test.retry.service.interfacetest.AccountServiceInterface;

/**
 * @Author: hzz
 * @Date: 2021/9/4 11:52:04
 * @Description: 动态代理演示 cglib
 */
public class CglibProxyTest {


	/**
	 * cglib  不管是接口还是类 都可以代理成功
	 *
	 * 演示抛出异常时候 进行重试，以及不抛出异常时候不重试
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		//1. 抛出异常 并且会执行3次重试 如下
		AccountServiceInterface proxyObject = (AccountServiceInterface) new CglibDynamicProxy().bind(new AccountServiceImpl());
		System.out.println(proxyObject.callAccountApi(null));
		//System.out.println(proxyObject.callAccountApiTwo(null));

		/*

		执行方法: callAccountApi
		出现异常 进行times递增 ,当前times值: 1
		执行方法: callAccountApi
		出现异常 进行times递增 ,当前times值: 2
		执行方法: callAccountApi
		出现异常 进行times递增 ,当前times值: 3
		发生钉钉信息 进行兜底策略
		Exception in thread "main" java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
			at com.xzll.test.retry.proxy.CglibDynamicProxy.intercept(CglibDynamicProxy.java:44)
			at com.xzll.test.retry.service.interfacetest.AccountServiceImpl$$EnhancerByCGLIB$$53776de0.callAccountApi(<generated>)
			at com.xzll.test.retry.test.CglibProxyTest.main(CglibProxyTest.java:24)
		Caused by: java.lang.reflect.InvocationTargetException
			at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
			at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
			at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
			at java.lang.reflect.Method.invoke(Method.java:498)
			at com.xzll.test.retry.proxy.CglibDynamicProxy.intercept(CglibDynamicProxy.java:36)
			... 2 more
		Caused by: java.lang.NullPointerException
			at com.xzll.test.retry.service.interfacetest.AccountServiceImpl.callAccountApi(AccountServiceImpl.java:20)
			... 7 more

		 */

		//2. 不抛出异常 ，所以不会重试
		AccountServiceParentClass proxyObject2 = (AccountServiceParentClass) new CglibDynamicProxy().bind(new AccountServiceSubClass());
		System.out.println(proxyObject2.callAccountApi("不抛异常"));//输出如下
		/*
		执行方法: callAccountApi
		调用账户信息接口成功
		 */

	}

}
