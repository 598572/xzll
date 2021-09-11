package com.xzll.test.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.xzll.common.base.Result;
import com.xzll.common.rabbitmq.ProducerService;
import com.xzll.test.entity.TestEntity;
import com.xzll.test.entity.TestMqConstant;
import com.xzll.test.entity.UserMQ;
import com.xzll.test.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@Slf4j
@Api(tags = "测试controller")
@RestController
@RequestMapping("/testController")
public class TestController {

	@Autowired
	private TestService testService;

	@Autowired
	private ProducerService producerService;

	@GetMapping("/test")
	@ApiOperation(value = "测试第一个接口查询", notes = "测试第一个接口查询")
	public Result<List<TestEntity>> test(@RequestParam(value = "param", required = true) String param) {
		log.info("测试接口");
		return Result.createOK(testService.testMybatiesPlus(param));
	}


	@GetMapping("/rabbitmqSendMsg")
	@ApiOperation(value = "测试rabbitmq发送消息", notes = "测试rabbitmq发送消息")
	public Result<List<TestEntity>> rabbitmqSendMsg(@RequestParam(value = "param", required = true) String param
			, @RequestParam(value = "age", required = true) Integer age) {

		UserMQ userMQ = new UserMQ();
		userMQ.setSex(10);
		userMQ.setAge(age);
		userMQ.setDesc(param);
		userMQ.setCtime(LocalDateTimeUtil.format(LocalDateTime.now(), NORM_DATETIME_FORMATTER));

		producerService.sendMsg(userMQ, TestMqConstant.TEST_ZHILIAN_EX, TestMqConstant.TEST_ZHILIAN_RK);

		return Result.createOK();
	}

}
