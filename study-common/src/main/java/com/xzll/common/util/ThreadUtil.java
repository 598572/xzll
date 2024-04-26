package com.xzll.common.util;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: hzz
 * @Date: 2021/9/12 13:19:30
 * @Description: 线程工具类
 */
public class ThreadUtil extends ThreadPoolExecutor {
	private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);




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

	//生成修饰了的对象executorService
//	private static ExecutorService executorService = TtlExecutors.getTtlExecutorService(executorService);



	public static void main(String[] args) {
		ThreadPoolExecutor hzz = getThreadPool(3, 10, 100, "测试线程池监控");
		for (int i = 0; i < 10; i++) {
			int finalI = i;
			hzz.execute(()->{
				System.out.println("开始执行任务，i："+finalI);
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
				}
				System.out.println("任务执行完毕，i： "+finalI);
			});
		}

	}



	public ThreadUtil(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,String poolName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		this.startTimes = new ConcurrentHashMap<>();
		this.poolName = poolName;
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
		ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat(threadName + "-task-%d").build();
		ThreadUtil threadUtil = new ThreadUtil(coreSize, maxSize, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), factory,threadName);
		//设置核心线程可以销毁
		threadUtil.allowCoreThreadTimeOut(true);
		return threadUtil;
	}

	public static ExecutorService getTtlThreadPool(int coreSize, int maxSize, int queueSize, String threadName) {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(coreSize, maxSize, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize));
		if (StringUtils.isNotEmpty(threadName)) {
			threadPool.setThreadFactory(new ThreadFactoryBuilder().setNameFormat(threadName + "-task-%d").build());
		}
		//设置核心线程可以销毁
		threadPool.allowCoreThreadTimeOut(true);
		ExecutorService ttlExecutor = TtlExecutors.getTtlExecutorService(threadPool);
		return ttlExecutor;
	}



	//------------------------------对线程池进行监控--------------------------------


	// 保存任务开始执行的时间，当任务结束时，用任务结束时间减去开始时间计算任务执行时间
	private ConcurrentHashMap<String, Date> startTimes;

	// 线程池名称，一般以业务名称命名，方便区分
	private String poolName;

	/**
	 * 调用父类的构造方法，并初始化HashMap和线程池名称
	 *
	 * @param corePoolSize
	 *            线程池核心线程数
	 * @param maximumPoolSize
	 *            线程池最大线程数
	 * @param keepAliveTime
	 *            线程的最大空闲时间
	 * @param unit
	 *            空闲时间的单位
	 * @param workQueue
	 *            保存被提交任务的队列
	 * @param poolName
	 *            线程池名称
	 */
	public ThreadUtil(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
							 String poolName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadMonitorUtil.EventThreadFactory(poolName));
		this.startTimes = new ConcurrentHashMap<>();
		this.poolName = poolName;
	}

	/**
	 * 线程池延迟关闭时（等待线程池里的任务都执行完毕），统计线程池情况
	 */
	@Override
	public void shutdown() {
		// 统计已执行任务、正在执行任务、未执行任务数量
		logger.info(String.format(this.poolName + " Going to shutdown. Executed tasks: %d, Running tasks: %d, Pending tasks: %d",
				this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size()));
		super.shutdown();
	}

	/**
	 * 线程池立即关闭时，统计线程池情况
	 */
	@Override
	public List<Runnable> shutdownNow() {
		// 统计已执行任务、正在执行任务、未执行任务数量
		logger.info(
				String.format(this.poolName + " Going to immediately shutdown. Executed tasks: %d, Running tasks: %d, Pending tasks: %d",
						this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size()));
		return super.shutdownNow();
	}

	/**
	 * 任务执行之前，记录任务开始时间
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		startTimes.put(String.valueOf(r.hashCode()), new Date());
	}

	/**
	 * 任务执行之后，计算任务结束时间
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
		Date finishDate = new Date();
		long diff = finishDate.getTime() - startDate.getTime();
		// 统计任务耗时、初始线程数、核心线程数、正在执行的任务数量、已完成任务数量、任务总数、队列里缓存的任务数量、池中存在的最大线程数、最大允许的线程数、线程空闲时间、线程池是否关闭、线程池是否终止
		logger.info(String.format(this.poolName
						+ "-pool-monitor: Duration: %d ms, PoolSize: %d, CorePoolSize: %d, Active: %d, Completed: %d, Task: %d, Queue: %d, LargestPoolSize: %d, MaximumPoolSize: %d,  KeepAliveTime: %d, isShutdown: %s, isTerminated: %s",
				diff, this.getPoolSize(), this.getCorePoolSize(), this.getActiveCount(), this.getCompletedTaskCount(), this.getTaskCount(),
				this.getQueue().size(), this.getLargestPoolSize(), this.getMaximumPoolSize(), this.getKeepAliveTime(TimeUnit.MILLISECONDS),
				this.isShutdown(), this.isTerminated()));
	}


}
