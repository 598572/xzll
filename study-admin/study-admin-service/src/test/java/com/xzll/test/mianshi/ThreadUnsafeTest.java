/**
 * 分析一下这个程序的输出
 * @author mashibing
 */

package com.xzll.test.mianshi;

import java.util.concurrent.CountDownLatch;

public class ThreadUnsafeTest {


	private static long n = 0L;

	public static void main(String[] args) throws Exception {

		Thread[] threads = new Thread[2];
		CountDownLatch latch = new CountDownLatch(threads.length);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0; j < 10000; j++) {
//					synchronized (T.class) {
					n++;
//					}
				}
				latch.countDown();
			});
		}

		for (Thread t : threads) {
			t.start();
		}

		latch.await();

		System.out.println(n);
	}

}
