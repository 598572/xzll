package com.xzll.test.point;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 时机：不用多说 销毁bean的时候
 */
@Component
public class DisposableBeanPoint implements DisposableBean {
	@Override
	public void destroy() throws Exception {
		System.out.println("[DisposableBeanPoint] DisposableBeanPoint");
	}
}
