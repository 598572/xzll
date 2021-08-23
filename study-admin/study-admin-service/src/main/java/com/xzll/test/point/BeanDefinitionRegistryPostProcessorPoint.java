package com.xzll.test.point;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 这个接口在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
 * 使用场景：你可以在这里动态注册自己的beanDefinition，可以加载classpath之外的bean
 *
 * 时机: refresh()的 this.invokeBeanFactoryPostProcessors(beanFactory); 方法中执行
 * 此时 bean的定义信息 都已经加载完毕 但是还没到实例化以及初始化阶段
 */
@Component
public class BeanDefinitionRegistryPostProcessorPoint implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("-----------------------[BeanDefinitionRegistryPostProcessor扩展点演示] # postProcessBeanDefinitionRegistry 开始--------------------------------------");
		System.out.println("[BeanDefinitionRegistryPostProcessor扩展点演示] # postProcessBeanDefinitionRegistry");
		System.out.println("时机: refresh()的 this.invokeBeanFactoryPostProcessors(beanFactory); 方法中执行; " +
				"此时 bean的定义信息 都已经加载完毕 但是还没到实例化以及初始化阶段");
		System.out.println("-----------------------[BeanDefinitionRegistryPostProcessor扩展点演示] # postProcessBeanDefinitionRegistry 结束--------------------------------------");
		System.out.println();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("-----------------------[BeanDefinitionRegistryPostProcessor扩展点演示] # postProcessBeanFactory 开始--------------------------------------");
		System.out.println("[BeanDefinitionRegistryPostProcessor扩展点演示] # postProcessBeanFactory");
		System.out.println("时机: refresh()的 this.invokeBeanFactoryPostProcessors(beanFactory); 方法中执行; " +
				"此时 bean的定义信息 都已经加载完毕 但是还没到实例化以及初始化阶段");
		System.out.println("-----------------------[BeanDefinitionRegistryPostProcessor扩展点演示] # postProcessBeanFactory 结束--------------------------------------");
		System.out.println();
	}
}
