package com.xzll.test.retry.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: hzz
 * @Date: 2021/9/3 18:20:00
 * @Description: jdk动态代理 （模板） 被代理对象必须得实现接口
 */
public class JdkDynamicProxy implements InvocationHandler {


	private static final int RETRY_TIME = 3;
	private Object subject;

	public JdkDynamicProxy(Object subject) {
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		int times = 0;

		while (times < RETRY_TIME) {
			try {
				// 当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
				return method.invoke(subject, args);
			} catch (Exception e) {
				times++;
				if (times >= RETRY_TIME) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	/**
	 * 获取动态代理
	 *
	 * @param realSubject 代理对象
	 */
	public static Object getProxy(Object realSubject, Class clazz) {
		//我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
		System.out.println("代理目标的接口是 : " + clazz);

		Class[] classes = clazz == null ? realSubject.getClass().getInterfaces() : new Class[]{clazz};

		InvocationHandler handler = new JdkDynamicProxy(realSubject);
		Object o = Proxy.newProxyInstance(handler.getClass().getClassLoader(),
				classes, handler);
		System.out.println("代理出来的对象是 : " + o);
		return o;
	}
}
