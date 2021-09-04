package com.xzll.test.retry.guaua;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hzz
 * @Date: 2021/9/3 16:05:08
 * @Description:
 */
@Service
@Slf4j
public class GuavaRetry {


	public Boolean test() throws Exception {
		//定义重试机制
		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
				//retryIf 重试条件
				.retryIfException()
				.retryIfRuntimeException()
				.retryIfExceptionOfType(Exception.class)
				.retryIfException(Predicates.equalTo(new Exception()))
				.retryIfResult(Predicates.equalTo(false))

				//等待策略：每次请求间隔1s
				.withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))

				//停止策略 : 尝试请求6次
				.withStopStrategy(StopStrategies.stopAfterAttempt(6))

				//时间限制 : 某次请求不得超过2s , 类似: TimeLimiter timeLimiter = new SimpleTimeLimiter();
				.withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))

				.build();

		//定义请求实现
		Callable<Boolean> callable = new Callable<Boolean>() {
			int times = 1;

			@Override
			public Boolean call() throws Exception {
				log.info("call times={}", times);
				times++;

				if (times == 2) {
					throw new NullPointerException();
				} else if (times == 3) {
					throw new Exception();
				} else if (times == 4) {
					throw new RuntimeException();
				} else if (times == 5) {
					return false;
				} else {
					return true;
				}

			}
		};
		//利用重试器调用请求
		return  retryer.call(callable);
	}

}
