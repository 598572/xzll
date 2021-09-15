package com.xzll.test.transactional;

import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.service.TransactionalInvalidation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: hzz
 * @Date: 2021/9/15 12:25:57
 * @Description: 事务不生效场景演示
 */
@Slf4j
public class TransactionalInvalidationTest extends StudyTestApplicationTest {

	@Autowired
	private TransactionalInvalidation transactionalInvalidation;



	@Test
	public void test(){
		transactionalInvalidation.saveA();
	}

}
