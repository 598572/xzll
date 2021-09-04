package com.xzll.test.retry;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.xzll.test.config.http.ResetTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Author: hzz
 * @Date: 2021/9/3 12:36:12
 * @Description: 基于Spring-retry进行重试的演示
 */
@Service
@Slf4j
public class RetryTest {

	@Autowired
	private ResetTemplateService resetTemplate;

//	@Scheduled(cron = "9 * * * * ? ")
	@Retryable(
			value = NullPointerException.class, // 指定异常进行重试
			include = {},// 处理异常
			exclude = {},// 例外异常
			maxAttempts = 3 // 最大重试次数
//			backoff = @Backoff( // 重试等待策略
//					delay = 2000L,// 重试间隔
//					multiplier = 1.5// 多次重试间隔系数2 、3、4.5
//			)
	)
	public void callAlibabaApi(){
		//组装请求数据
		HashMap<String, String> queryParams = new HashMap<>(16);
		queryParams.put("key1", "value1");
		queryParams.put("key2", "values2");

		log.info("do  :{}", LocalDateTimeUtil.now());
		//Object response = resetTemplate.getWithParams("http://www.6tie.net/p/1166764.html", queryParams, Object.class);

		try {
			throw new NullPointerException();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	@Recover
	public int recover12(NullPointerException e) {
		log.info("service retry after Recover => {}", e.getMessage());
		return 0;
	}

}
