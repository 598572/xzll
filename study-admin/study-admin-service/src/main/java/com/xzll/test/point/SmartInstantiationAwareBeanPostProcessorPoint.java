package com.xzll.test.point;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * 具体的调用地方在下边有解释 TODO 目前对该类不做深究
 */
@Component
public class SmartInstantiationAwareBeanPostProcessorPoint implements SmartInstantiationAwareBeanPostProcessor {


	@Override
	public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
//		System.out.println("[SmartInstantiationAwareBeanPostProcessorPoint] predictBeanType " + beanName);
		return beanClass;
	}

	@Override
	public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
//		System.out.println("[SmartInstantiationAwareBeanPostProcessorPoint] determineCandidateConstructors " + beanName);
		return null;
	}

	@Override
	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
//		System.out.println("[SmartInstantiationAwareBeanPostProcessorPoint] getEarlyBeanReference " + beanName);
		return bean;
	}

	/*

	AbstractAutowireCapableBeanFactory类的这个方法 predictBeanType

	protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
        Class<?> targetType = this.determineTargetType(beanName, mbd, typesToMatch);
        if (targetType != null && !mbd.isSynthetic() && this.hasInstantiationAwareBeanPostProcessors()) {
            Iterator var5 = this.getBeanPostProcessors().iterator();

            while(var5.hasNext()) {
                BeanPostProcessor bp = (BeanPostProcessor)var5.next();
                if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                    SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
                    Class<?> predicted = ibp.predictBeanType(targetType, beanName);
                    if (predicted != null && (typesToMatch.length != 1 || FactoryBean.class != typesToMatch[0] || FactoryBean.class.isAssignableFrom(predicted))) {
                        return predicted;
                    }
                }
            }
        }

        return targetType;
    }

	 */

}
