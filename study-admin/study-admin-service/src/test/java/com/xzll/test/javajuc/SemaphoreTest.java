package com.xzll.test.javajuc;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hzz
 * @Date: 2023/10/18 15:23:21
 * @Description:
 */
public class SemaphoreTest {
	public static BlockingQueue<Runnable> asyncSenderThreadPoolQueue;
	//测试
	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor executorService = new ThreadPoolExecutor(4, 4, 1000 * 60, TimeUnit.MILLISECONDS, SemaphoreTest.asyncSenderThreadPoolQueue = new LinkedBlockingQueue<Runnable>(500), new ThreadFactory() {
			private final AtomicInteger threadIndex = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "SemaphoreTest_" + this.threadIndex.incrementAndGet());
			}
		});
		Semaphore semaphore = new Semaphore(3);
		for (int i = 0; i < 12; i++) {
			executorService.execute(() -> {
				try {
					// 先获取许可，没有许可则阻塞
					if (semaphore.availablePermits() == 0) {
						System.out.println("分配的3个许可都用完了，调用acquire的话会阻塞等待直到有许可可用");
					}
					semaphore.acquire();
					System.out.println(Thread.currentThread().getName() + " 【消费】了一个许可，当前可用许可数量为: " + semaphore.availablePermits());
					ThreadUtil.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 释放许可
					semaphore.release();
					System.out.println(Thread.currentThread().getName() + " 《释放》了一个许可，当前可用许可数量为: " + semaphore.availablePermits());
					System.out.println();
				}
			});
		}
	}

}
