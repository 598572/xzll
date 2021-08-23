package com.xzll.test.point;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 时机: refresh()的 this.invokeBeanFactoryPostProcessors(beanFactory); 方法中执行
 * 此时 bean的定义信息 都已经加载完毕 但是还没到实例化以及初始化阶段
 * 注意: 该类的执行事件和 BeanDefinitionRegistryPostProcessorPoint是一样的 Juin见 BeanDefinitionRegistryPostProcessorPoint中的描述即可
 */
@Component
public class BeanFactoryPostProcessorPoint implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("-----------------------[BeanFactoryPostProcessor扩展点演示] # postProcessBeanFactory 开始--------------------------------------");
		System.out.println("[BeanFactoryPostProcessor扩展点演示] # postProcessBeanFactory");
		System.out.println("时机: refresh()的 this.invokeBeanFactoryPostProcessors(beanFactory); 方法中执行; " +
				"此时 bean的定义信息 都已经加载完毕 但是还没到实例化以及初始化阶段");
		System.out.println("-----------------------[BeanFactoryPostProcessor扩展点演示] # postProcessBeanFactory 结束--------------------------------------");
		System.out.println();
	}
}
