package com.xzll.test.point;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 应该是在创建bean的时候 具体调用代码先不深究了
 */
@Component
public class FactoryBeanPoint implements FactoryBean<FactoryBeanPoint.FactoryInnerBeanPoint> {
	@Override
	public FactoryBeanPoint.FactoryInnerBeanPoint getObject() throws Exception {
		System.out.println("[FactoryBean] getObject");
		return new FactoryBeanPoint.FactoryInnerBeanPoint();
	}

	@Override
	public Class<?> getObjectType() {
		return FactoryBeanPoint.FactoryInnerBeanPoint.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public static class FactoryInnerBeanPoint {

	}

}
