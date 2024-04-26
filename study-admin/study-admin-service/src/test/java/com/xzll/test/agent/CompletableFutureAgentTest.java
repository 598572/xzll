package com.xzll.test.agent;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: hzz
 * @Date: 2024/1/12 10:16:28
 * @Description:
 */
@Slf4j
public class CompletableFutureAgentTest {
	public static void main(String[] args) throws InterruptedException {
		ThreadContext.put("xzll_trace_id", UUID.fastUUID().toString());
		CompletableFuture.runAsync(()->{
			log.info("嘿嘿嘿");

			log.info("哈哈哈");
		});
		Thread.sleep(100000000L);
	}
}
