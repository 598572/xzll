package com.xzll.common.email.selector;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author: hzz
 * @Date: 2021/9/18 18:47:58
 * @Description:
 */
public class MyImportBeanDefinitionRegistrar
		implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
		//指定Bean定义信息
		RootBeanDefinition beanDefinition =
				new RootBeanDefinition
						(TestRegisterBeanDefinitions.class);
		//注册一个Bean，指定bean名，
		// 这里指定 hzzxzll 在注入时候使用
		//@Qualifier(value = "hzzxzll")
		beanDefinitionRegistry
				.registerBeanDefinition("hzzxzll",
						beanDefinition);
	}

}
