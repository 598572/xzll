package com.xzll.test.retry;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.google.common.base.Predicates;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @Author: hzz
 * @Date: 2021/9/3 17:23:20
 * @Description:
 */
public class GuavaRetry {

	public static void main(String[] args) {
		Callable<Boolean> callable = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// do something useful here
				System.out.println("call");
				throw new RuntimeException();
			}
		};

		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
				.retryIfResult(Predicates.isNull())
				.retryIfExceptionOfType(IOException.class)
				.retryIfRuntimeException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.build();
		try {
			retryer.call(callable);
		} catch (RetryException | ExecutionException e) {
			e.printStackTrace();
		}

	}

}
