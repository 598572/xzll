package com.xzll.test.service.impl;

import com.xzll.common.base.Result;
import com.xzll.test.entity.TestEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description: 为了测试非public方法 事务注解不生效的代码
 */
@Slf4j
@Api(tags = "测试controller")
@RestController
@RequestMapping("/nopublic")
public class TestNoPublicTran {

	@Autowired
	private TransactionalInvalidationImpl transactionalInvalidation;
	@GetMapping("/nopublic")
	@ApiOperation(value = "strategy测试策略模式", notes = "strategy测试策略模式")
	public Result<List<TestEntity>> nopublic(@RequestParam(value = "status", required = true) Integer status) {
		transactionalInvalidation.noPublicTransactional();
		return Result.createOK();
	}

}
