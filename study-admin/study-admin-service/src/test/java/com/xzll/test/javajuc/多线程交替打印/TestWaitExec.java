package com.xzll.test.javajuc.多线程交替打印;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: hzz
 * @Date: 2023/2/10 11:10:07
 * @Description:
 */
public class TestWaitExec {

	private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();


	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			list.add(i);
		}
		long currentTimeMillis = System.currentTimeMillis();
		CountDownLatch countDownLatch = new CountDownLatch(list.size());

		list.forEach(x->{
			fixedThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						countDownLatch.countDown();
						System.out.println("减减");
					}
				}
			});
		});
		System.out.println("提交完成");
		try {
			countDownLatch.await();
			System.out.println("执行完了 ");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("执行耗时："+(System.currentTimeMillis()-currentTimeMillis));

	}
}
