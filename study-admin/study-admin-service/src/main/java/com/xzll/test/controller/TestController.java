package com.xzll.test.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.google.common.collect.Maps;
import com.xzll.common.base.Result;
import com.xzll.common.rabbitmq.producer.ProducerService;
import com.xzll.test.entity.TestEntity;
import com.xzll.test.entity.TestMqConstant;
import com.xzll.test.entity.UserMQ;
import com.xzll.test.service.TestService;
import com.xzll.test.strategy.ApproveStrategy;
import com.xzll.test.strategy.factory.StrategyFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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


	@Resource
	private StrategyFactory<Integer, ApproveStrategy> approveStrategyFactory;

	/**
	 * 测试策略模式的接口
	 *
	 * @param status
	 * @return
	 */
	@GetMapping("/strategy")
	@ApiOperation(value = "strategy测试策略模式", notes = "strategy测试策略模式")
	public Result<List<TestEntity>> strategy(@RequestParam(value = "status", required = true) Integer status) {
		log.info("strategy测试策略模式");
		String s = approveStrategyFactory.getStrategy(status, true).approveByRefundStatus(new Object(), new Object(), Maps.newHashMap());
		System.out.println("strategy测试策略模式返回结果" + s);
		return Result.createOK();
	}

}
