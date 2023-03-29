/**
 * 分析一下这个程序的输出
 *
 * @author mashibing
 */

package com.xzll.test.mianshi;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SyncWorkOnStaticsMethodTest {


	private static int cpuCount = Runtime.getRuntime().availableProcessors();

	private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(cpuCount, cpuCount * 2, 20,
			TimeUnit.SECONDS, new LinkedBlockingQueue<>(20000));

	static {
		System.out.println("我的cpu颗数: " + cpuCount);
		threadPool.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("测试++操作" + "-task-%d").build());
	}

	//成员变量，可被多个线程 共享
	private static long n = 0L;


	public static void main(String[] args) throws Exception {

		//多弄点，不然不好看出结果
		CountDownLatch countDownLatch = new CountDownLatch(10000);

		for (int i = 1; i < 10001; i++) {
			threadPool.execute(() -> {
				incr(countDownLatch);
			});
		}
		//等待所有线程 执行完后再打印 ++的结果
		countDownLatch.await();
		System.out.println("最终++的结果: " + n);
	}

	//修饰静态方法，使用的锁对象是当前类
	private static synchronized void incr(CountDownLatch countDownLatch) {
		try {
			n++;
		} finally {
			countDownLatch.countDown();
		}
	}

}
