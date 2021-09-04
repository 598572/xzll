package com.xzll.test.retry.utils;

import java.lang.annotation.*;

/**
 * @Author: hzz
 * @Date: 2021/9/4 19:52:04
 * @Description: 重试注解 用在方法上。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retryable {

	/**
	 * 当异常类型是给定值时候进行重试
	 *
	 * @return
	 */
	Class<? extends Throwable> value() default RuntimeException.class;

	/**
	 * 最大重试次数
	 *
	 * @return 最大尝试次数（包括第一次失败），默认为 3
	 */
	int maxAttempts() default 3;

	/**
	 * 重试间隔系数 ( 第一次 1*2 = 2ms ; 第二次 2*2 = 4ms ; 第三次 3*3 = 6ms ; ) 单位毫秒
	 *
	 * @return 重试间隔
	 */
	int retryInterval() default 2;

}
