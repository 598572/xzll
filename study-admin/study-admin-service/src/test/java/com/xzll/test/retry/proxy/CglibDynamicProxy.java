package com.xzll.test.retry.proxy;

import com.xzll.test.retry.constant.RetryConstant;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: hzz
 * @Date: 2021/9/3 18:44:00
 * @Description: cglib代理模板
 */
public class CglibDynamicProxy implements MethodInterceptor {

	private Object target;

	public Object bind(Object obj) {
		Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(obj.getClass().getInterfaces());
		enhancer.setSuperclass(obj.getClass());
		this.target = obj;
		enhancer.setCallback(this);
		return enhancer.create();
	}

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		int times = 0;

		while (times < RetryConstant.MAX_TIMES) {
			try {
				System.out.println("执行方法: " + method.getName());
				//通过代理子类调用父类的方法
				return method.invoke(target, objects);//如果此处为 o的话 将会陷入死循环 此处传入target即非代理对象 避免了使用代理对象
				//return methodProxy.invokeSuper(o, objects);
			} catch (Exception e) {
				times++;
				System.out.println("出现异常 进行times递增 ,当前times值: " + times);
				if (times >= RetryConstant.MAX_TIMES) {
					//发送钉钉信息 进行兜底策略
					System.out.println("发生钉钉信息 进行兜底策略");
					throw new RuntimeException(e);
				}
			}
		}
		return o;
	}
}
