package com.xzll.test.service.impl;

import com.xzll.test.entity.TableBDO;
import com.xzll.test.mapper.TableBMapper;
import com.xzll.test.service.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: hzz
 * @Date: 2021/9/15 12:16:23
 * @Description:
 */
@Service
@Slf4j
public class BImpl implements B {


	@Autowired
	private TableBMapper tableBMapper;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public void saveB() {

		TableBDO tableBDO = new TableBDO();
		tableBDO.setBDescription("第一次插入B表描述");

		tableBMapper.insert(tableBDO);

		int i = 1 / 0;//造个异常


		TableBDO tableBDO2 = new TableBDO();
		tableBDO2.setBDescription("第二次插入B表描述");

		tableBMapper.insert(tableBDO2);
	}
}
