package com.xzll.test.retry.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hzz
 * @Date: 2021/9/4 19:35:30
 * @Description: Retryable 注解切面，对方法上有Retryable的进行拦截 ， 并重试。
 */
@Aspect
@Component
@Slf4j
public class RetryAspect {

	//定义切点
	@Pointcut("@annotation(com.xzll.test.retry.utils.Retryable)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Method method = getCurrentMethod(point);
		Retryable retryable = method.getAnnotation(Retryable.class);

		//1. 最大次数判断
		int maxAttempts = retryable.maxAttempts();
		if (maxAttempts <= 1) {
			return point.proceed();
		}

		//2. 异常处理 进行重试 默认三次 ，重试间隔递增
		AtomicInteger times = new AtomicInteger(1);
		final Class<? extends Throwable> exceptionClass = retryable.value();

		int retryInterval = retryable.retryInterval();

		while (times.get() <= maxAttempts) {
			System.out.println();
			log.info("第 {} 次{}", times.get(), times.get() == 1 ? "执行" : "执行重试");
			try {
				if (times.get() == 1) {
					return point.proceed();
				}
				if (times.get() > 1 && times.get() <= maxAttempts) {
					int sleep = times.get() * retryInterval * 1000;
					Thread.sleep((long) sleep);//在循环中调用 'Thread.sleep()'，可能一直等待
					log.info("sleep时间: {} ", sleep);
					return point.proceed();
				}
			} catch (Throwable e) {

				log.info("出现异常 进行times递增 ,当前times值: {}", times.get());
				times.getAndIncrement();

				// 超过最大重试次数 or 不属于当前处理异常
				if (times.get() > maxAttempts ||
						!e.getClass().isAssignableFrom(exceptionClass)) {
					//异步 发送钉钉通知
					System.out.println();
					log.error("第{}次重试时候，超出最大值{},将执行兜底策略比如 (发送钉钉信息 ..... )", times, maxAttempts);
					throw new Throwable(e);
				}
			}
		}

		return null;
	}

	/**
	 * 获取当前方法
	 *
	 * @param point
	 * @return
	 */
	private Method getCurrentMethod(ProceedingJoinPoint point) {
		try {
			Signature sig = point.getSignature();
			MethodSignature methodSignature = (MethodSignature) sig;
			Object target = point.getTarget();
			return target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	/*

	around输出内容见下:

	2021-09-04 20:57:43.910  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 第 1 次执行
	noRetry: 调用账户信息接口成功

	2021-09-04 20:57:43.921  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 第 1 次执行
	2021-09-04 20:57:43.921  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 出现异常 进行times递增 ,当前times值: 1

	2021-09-04 20:57:43.921  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 第 2 次执行重试
	2021-09-04 20:57:49.923  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : sleep时间: 6000
	2021-09-04 20:57:49.923  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 出现异常 进行times递增 ,当前times值: 2

	2021-09-04 20:57:49.923  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 第 3 次执行重试
	2021-09-04 20:57:58.925  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : sleep时间: 9000
	2021-09-04 20:57:58.926  INFO 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 出现异常 进行times递增 ,当前times值: 3

	2021-09-04 20:57:58.926 ERROR 35144 --- [           main] com.xzll.test.retry.utils.RetryAspect    : 第4次重试时候，超出最大值3,将执行兜底策略比如 (发送钉钉信息 ..... )

	java.lang.reflect.UndeclaredThrowableException
		at com.xzll.test.retry.utils.service.AccountServiceImplWithSpringWithSpring$$EnhancerBySpringCGLIB$$3a8020fd.callAccountApi(<generated>)
		at com.xzll.test.retry.utils.RetryUtilsTest.queryAccountProxyClass(RetryUtilsTest.java:25)
		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
		at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
		at java.lang.reflect.Method.invoke(Method.java:498)
		at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
		at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
		at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
		at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
		at org.springframework.test.context.junit4.statements.RunBeforeTestExecutionCallbacks.evaluate(RunBeforeTestExecutionCallbacks.java:74)
		at org.springframework.test.context.junit4.statements.RunAfterTestExecutionCallbacks.evaluate(RunAfterTestExecutionCallbacks.java:84)
		at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
		at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
		at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
		at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
		at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:251)
		at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
		at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
		at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
		at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
		at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
		at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
		at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
		at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
		at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
		at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
		at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
		at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:69)
		at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
		at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:220)
		at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:53)
	Caused by: java.lang.Throwable: java.lang.NullPointerException
		at com.xzll.test.retry.utils.RetryAspect.around(RetryAspect.java:71)
		at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
		at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
		at java.lang.reflect.Method.invoke(Method.java:498)
		at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:644)
		at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:633)
		at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)
		at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:175)
		at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:93)
		at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
		at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)
		... 32 more
	Caused by: java.lang.NullPointerException
		at com.xzll.test.retry.utils.service.AccountServiceImplWithSpringWithSpring.callAccountApi(AccountServiceImplWithSpringWithSpring.java:22)
		at com.xzll.test.retry.utils.service.AccountServiceImplWithSpringWithSpring$$FastClassBySpringCGLIB$$a3dfb087.invoke(<generated>)
		at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
		at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749)
		at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
		at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:88)
		at com.xzll.test.retry.utils.RetryAspect.around(RetryAspect.java:58)
		... 43 more

	 */
}
