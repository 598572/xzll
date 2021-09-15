package com.xzll.test.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author: hzz
 * @Date: 2021/9/15 18:10:14
 * @Description:
 *
 * 这个接口在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
 * 使用场景：你可以在这里动态注册自己的beanDefinition，可以加载classpath之外的bean
 * 时机: refresh()的 this.invokeBeanFactoryPostProcessors(beanFactory); 方法中执行
 * 此时 bean的定义信息 都已经加载完毕 但是还没到实例化以及初始化阶段
 */
@Component
public class TransactionalBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

	//为了解决代理类获取不到的问题 我们在这里将 internalAutoProxyCreator对应的
	//bean定义中的 exposeProxy改为true 其实 //@EnableAspectJAutoProxy(exposeProxy = true)也是这么做的
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
			BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop." +
					"config.internalAutoProxyCreator");
			definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

	}
}
