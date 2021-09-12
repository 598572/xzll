package com.xzll.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hzz
 * @Date: 2021/9/12 13:19:30
 * @Description: 线程工具类
 */
public class ThreadUtil {
	/**
	 * 线程池
	 *
	 * @param coreSize  最小线程数
	 * @param maxSize   最大线程池
	 * @param queueSize 队列大小
	 * @return 结果
	 */
	public static ThreadPoolExecutor getThreadPool(int coreSize, int maxSize, int queueSize) {
		return getThreadPool(coreSize, maxSize, queueSize, null);
	}

	/**
	 * 线程池
	 *
	 * @param coreSize   最小线程数
	 * @param maxSize    最大线程池
	 * @param queueSize  队列大小
	 * @param threadName 设置线程名称
	 * @return 结果
	 */
	public static ThreadPoolExecutor getThreadPool(int coreSize, int maxSize, int queueSize, String threadName) {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(coreSize, maxSize, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize));
		if (StringUtils.isNotEmpty(threadName)) {
			threadPool.setThreadFactory(new ThreadFactoryBuilder().setNameFormat(threadName + "-task-%d").build());
		}
		//设置核心线程可以销毁
		threadPool.allowCoreThreadTimeOut(true);
		return threadPool;
	}
}
