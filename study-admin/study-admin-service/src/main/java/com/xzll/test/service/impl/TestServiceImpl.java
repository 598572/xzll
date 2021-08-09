package com.xzll.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzll.test.entity.TestEntity;
import com.xzll.test.mapper.TestMapper;
import com.xzll.test.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:32
 * @Description:
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestMapper testMapper;

	@Override
	public List<TestEntity> testMybatiesPlus(String param) {

		TestEntity model = new TestEntity().setName(param).setSex(1);
		testMapper.insert(model);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LambdaQueryWrapper<TestEntity> queryWrapper = new LambdaQueryWrapper<>();
		List<TestEntity> testEntities = testMapper.selectList(queryWrapper);
		return testEntities;
	}
}
