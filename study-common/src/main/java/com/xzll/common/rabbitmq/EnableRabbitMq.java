package com.xzll.common.rabbitmq;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RabbitConfig.class)//导入配置类 使spring能够扫描到该配置类并进行bean的加载
public @interface EnableRabbitMq {
}
