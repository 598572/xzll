//package com.xzll.common.util;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Date;
//import java.util.concurrent.*;
//
///**
// * @Author: hzz
// * @Date: 2023/3/6 17:08:33
// * @Description: 线程池监控工具
// */
//public class ForkJoinMonitorUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(ForkJoinMonitorUtil.class);
//
//	// 保存任务开始执行的时间，当任务结束时，用任务结束时间减去开始时间计算任务执行时间
//	private static volatile ConcurrentHashMap<String, Date> startTimes;
//
//	// 线程池名称，一般以业务名称命名，方便区分
//	private static volatile String poolName;
//
//	/**
//	 * 任务执行之前，记录任务开始时间
//	 */
//	public static void beforeExecute(Runnable r) {
//		startTimes.put(String.valueOf(r.hashCode()), new Date());
//	}
//
//	/**
//	 * 任务执行之后，计算任务结束时间
//	 */
//	public static void afterExecute(Executor executor,Runnable r) {
//
//		Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
//		long diff = System.currentTimeMillis() - startDate.getTime();
//		if (executor instanceof ForkJoinPool) {
//			ForkJoinPool forkJoinPool = (ForkJoinPool) executor;
//			// 统计任务耗时、初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止
//			logger.info(String.format("forkJoinPool-monitor: Duration: %d ms, PoolSize: %d, activeThreadCount: %d, getRunningThreadCount: %d, getQueuedSubmissionCount: %d, getQueuedTaskCount: %d, isTerminated: %s",
//					diff, forkJoinPool.getPoolSize(), forkJoinPool.getActiveThreadCount(), forkJoinPool.getRunningThreadCount(), forkJoinPool.getQueuedSubmissionCount(), forkJoinPool.getQueuedTaskCount(),
//					forkJoinPool.isTerminated()));
//		}
//		if (executor instanceof ThreadPoolExecutor){
//			ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
//			// 统计任务耗时、初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止
//			logger.info(String.format("threadPoolExecutor-monitor: Duration: %d ms, PoolSize: %d, CorePoolSize: %d, Active: %d, Completed: %d, Task: %d, Queue: %d, LargestPoolSize: %d, MaximumPoolSize: %d,  KeepAliveTime: %d, isShutdown: %s, isTerminated: %s",
//					diff, poolExecutor.getPoolSize(), poolExecutor.getCorePoolSize(), poolExecutor.getActiveCount(), poolExecutor.getCompletedTaskCount(), poolExecutor.getTaskCount(),
//					poolExecutor.getQueue().size(), poolExecutor.getLargestPoolSize(), poolExecutor.getMaximumPoolSize(), poolExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS),
//					poolExecutor.isShutdown(), poolExecutor.isTerminated()));
//		}
//
//
//	}
//
//
//}
