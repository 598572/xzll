package com.xzll.test.point;


import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;

/**
 * 太多了 时机应该是在 createBean 的 populateBean 时候 TODO 目前对该类不做深究
 */
@Component
public class InstantiationAwareBeanPostProcessorPoint implements InstantiationAwareBeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//		System.out.println("[InstantiationAwareBeanPostProcessorPoint] before initialization " + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//		System.out.println("[InstantiationAwareBeanPostProcessorPoint] after initialization " + beanName);
		return bean;
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
//		System.out.println("[InstantiationAwareBeanPostProcessorPoint] before instantiation " + beanName);
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
//		System.out.println("[InstantiationAwareBeanPostProcessorPoint] after instantiation " + beanName);
		return true;
	}

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
//		System.out.println("[InstantiationAwareBeanPostProcessorPoint] postProcessPropertyValues " + beanName);
		return pvs;
	}
}
