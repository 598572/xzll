package com.xzll.test.controller;

import com.xzll.admin.api.dto.AdminUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.xzll.test.config.ThreadPoolConfig.XZLL_TRACE_ID;

/**
 * @Author: hzz
 * @Date: 2023/2/21 11:01:56
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/adminUser")
public class ThreadErrorTestController {

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@GetMapping("/testThreadPool")
	public List<AdminUserDTO> testThreadPool(@RequestParam(value = "username", required = false) String username) {
		log.info("############请求开始############");
		ThreadContext.put(XZLL_TRACE_ID, UUID.randomUUID().toString().toLowerCase().replaceAll("-", ""));
		threadPoolTaskExecutor.execute(()->{
			log.info("哈哈");
			try {
				Thread.sleep(100000);
				log.info("当前线程池情况_active:{},poolSize:{},queueSize:{}",threadPoolTaskExecutor.getActiveCount(),threadPoolTaskExecutor.getPoolSize(),threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size());
			} catch (InterruptedException e) {
				log.error("出现异常e:",e);
			}
		});
		log.info("###########一次任务执行完成############");
		return null;
	}

}
