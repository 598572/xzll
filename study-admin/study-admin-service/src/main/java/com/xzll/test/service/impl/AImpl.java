package com.xzll.test.service.impl;

import com.xzll.test.entity.TableADO;
import com.xzll.test.mapper.TableAMapper;
import com.xzll.test.service.A;
import com.xzll.test.service.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: hzz
 * @Date: 2021/9/15 12:16:23
 * @Description:
 */
@Service
@Slf4j
public class AImpl implements A {

	@Autowired
	private TableAMapper tableAMapper;

	@Autowired
	private B b;

//	@Override
//	@Transactional(propagation = Propagation.REQUIRED)
//	public void saveA() {
//
//		TableADO tableADO = new TableADO();
//		tableADO.setADescription("第一次插入A表描述");
//
//		tableAMapper.insert(tableADO);
//		b.saveB();
//	}


//	@Override
//	//@Transactional(propagation = Propagation.REQUIRED)
//	public void saveA() {
//
//		TableADO tableADO = new TableADO();
//		tableADO.setADescription("第一次插入A表描述");
//
//		tableAMapper.insert(tableADO);
//		b.saveB();
//	}

//	@Override
//	public void saveA() {
//
//		TableADO tableADO = new TableADO();
//		tableADO.setADescription("第一次插入A表描述");
//
//		tableAMapper.insert(tableADO);
//		b.saveB();
//	}


//	@Override
//	@Transactional(propagation = Propagation.SUPPORTS)
//	public void saveA() {
//
//		TableADO tableADO = new TableADO();
//		tableADO.setADescription("第一次插入A表描述");
//
//		tableAMapper.insert(tableADO);
//		b.saveB();
//	}


//	@Override
//	public void saveA() {
//
//		TableADO tableADO = new TableADO();
//		tableADO.setADescription("第一次插入A表描述");
//
//		tableAMapper.insert(tableADO);
//		b.saveB();
//	}


	@Override
	public void saveA() {

		TableADO tableADO = new TableADO();
		tableADO.setADescription("第一次插入A表描述");

		tableAMapper.insert(tableADO);
		b.saveB();

	}


//		try {
	//b.saveB();
//		}catch (Exception e){
//			e.printStackTrace();
//		}
	//int i = 1 / 0;//造个异常

}
