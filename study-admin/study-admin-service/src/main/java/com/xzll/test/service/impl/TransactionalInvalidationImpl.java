package com.xzll.test.service.impl;

import com.xzll.test.entity.TableADO;
import com.xzll.test.mapper.TableAMapper;
import com.xzll.test.service.TransactionalInvalidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: hzz
 * @Date: 2021/9/15 16:19:00
 * @Description: ((TransactionalInvalidationImpl) AopContext.currentProxy()).saveA2();
 */
@Service
@Slf4j
public class TransactionalInvalidationImpl implements TransactionalInvalidation {

	@Autowired
	private TableAMapper tableAMapper;

	@Override
	public void saveA() {
		TableADO tableADO = new TableADO();
		tableADO.setADescription("saveA");

		tableAMapper.insert(tableADO);
		((TransactionalInvalidationImpl) AopContext.currentProxy()).saveA2();
		//saveA2(); 这种调用 事务注解不会生效
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void saveA2() {

		TableADO tableADO2 = new TableADO();
		tableADO2.setADescription("saveA2");

		tableAMapper.insert(tableADO2);
		int i = 1 / 0;
	}

	/**
	 * 非public 事务注解也不会生效
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ArithmeticException.class)
	protected void noPublicTransactional() {
		TableADO tableADO = new TableADO();
		tableADO.setADescription("saveA");

		tableAMapper.insert(tableADO);

		int i = 1 / 0;
	}


}
