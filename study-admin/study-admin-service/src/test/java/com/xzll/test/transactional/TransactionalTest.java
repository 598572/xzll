package com.xzll.test.transactional;

import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.service.A;
import com.xzll.test.service.B;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: hzz
 * @Date: 2021/9/15 12:25:57
 * @Description:
 */
@Slf4j
public class TransactionalTest  extends StudyTestApplicationTest {

	@Autowired
	private A a;

	@Autowired
	private B b;



	@Test
	public void test(){

		a.saveA();
	}

}
