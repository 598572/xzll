package com.xzll.common.alarm;

import com.xzll.common.email.selector.AlarmServiceSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: hzz
 * @Date: 2021/9/18 16:33:34
 * @Description: 加载springboot启动类上 用于指定开启哪些报警方式
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(AlarmServiceSelector.class)
public @interface EnableAlarmNotice {
	 String[] types() default {"ding_ding"};
}
