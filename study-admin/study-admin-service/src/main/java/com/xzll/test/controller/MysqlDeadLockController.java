package com.xzll.test.controller;

import com.google.common.collect.Lists;
import com.xzll.test.ao.PrePayOrderAo;
import com.xzll.test.service.PrePayOrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: hzz
 * @Date: 2021/9/29 17:52:46
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/mysql")
public class MysqlDeadLockController {

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private PrePayOrderRecordService prePayOrderRecordService;

	@PostMapping("/deadLock/test")
	public List<Long> deadLock(@RequestBody PrePayOrderAo ao) {
		List<Long> add = Lists.newArrayList();

		//模拟并发 预支付
		Integer begin = ao.getBegin();
		for (int j = begin; j < ao.getCount(); j++) {
			PrePayOrderAo prePayOrderAo = new PrePayOrderAo();
			prePayOrderAo.setChannelId(10);
			prePayOrderAo.setStatus(1);
			prePayOrderAo.setOrderPrice(200);
			prePayOrderAo.setOrderId(j);

			taskExecutor.execute(()->{
				//用户预支付
				prePayOrderRecordService.prePayOrder(prePayOrderAo);
			});
		}
		return add;
	}

	@PostMapping("/deadLock/rangeGap")
	public List<Long> rangeGap(@RequestBody PrePayOrderAo ao) {
		List<Long> add = Lists.newArrayList();

		//模拟并发 预支付
		int i=ao.getBegin();
		for (int j = i; j <= ao.getEnd(); j++) {
			PrePayOrderAo prePayOrderAo = new PrePayOrderAo();
			prePayOrderAo.setChannelId(10);
			prePayOrderAo.setStatus(1);
			prePayOrderAo.setOrderPrice(200);
			prePayOrderAo.setOrderId(j);

			taskExecutor.execute(()->{
				//用户预支付¸
				try {
					//模拟其他接口的根据orderId for update查询数据
					prePayOrderRecordService.findForUpdate(prePayOrderAo);
					//模拟预支付
					prePayOrderRecordService.prePayOrder(prePayOrderAo);
				} catch (Exception exception) {
					log.error("预支付失败e",exception);
				}
			});
		}
		return add;
	}
}
