package com.xzll.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

	public static final String XZLL_TRACE_ID = "xzll_trace_id";

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("###availableProcessors："+availableProcessors);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(availableProcessors * 10);
		executor.setMaxPoolSize(availableProcessors * 10);
		executor.setQueueCapacity(2000);
		executor.setKeepAliveSeconds(10);

//		executor.setTaskDecorator(new TraceTaskDecorator());
		executor.setThreadNamePrefix("push-center-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

		return executor;
	}

	/**
	 * 官方大意：这是一个执行回调方法的装饰器，主要应用于传递上下文，或者提供任务的监控/统计信息
	 */
//	public static class TraceTaskDecorator implements TaskDecorator {
//
//		@Override
//		public Runnable decorate(Runnable runnable) {
//			String traceId = ThreadContext.get(XZLL_TRACE_ID);
//			return () -> {
//				try {
//					//将主线程的信息(这里只是traceId就够了)，设置到子线程中
//					ThreadContext.put(XZLL_TRACE_ID, traceId);
//					//执行线程池任务
//					runnable.run();
//				} finally {
//					//线程结束，清空这些信息，否则可能造成内存泄漏
//					ThreadContext.remove(XZLL_TRACE_ID);
//				}
//			};
//		}
//	}


}
