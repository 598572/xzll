package com.xzll.test.redis.cache;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Author: hzz
 * @Date: 2021/8/30 14:37:17
 * @Description: 线程监控演示
 */
public class Mon {

	private static ExecutorService es = new ThreadPoolExecutor(50, 100, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(100000));

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 100000; i++) {
			es.execute(() -> {
				System.out.print(1);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}

		ThreadPoolExecutor tpe = ((ThreadPoolExecutor) es);

		while (true) {
			System.out.println();

			int queueSize = tpe.getQueue().size();
			System.out.println("当前排队线程数：" + queueSize);

			int activeCount = tpe.getActiveCount();
			System.out.println("当前活动线程数：" + activeCount);

			long completedTaskCount = tpe.getCompletedTaskCount();
			System.out.println("执行完成线程数：" + completedTaskCount);

			long taskCount = tpe.getTaskCount();
			System.out.println("总线程数：" + taskCount);

			Thread.sleep(3000);
		}
	}
}
