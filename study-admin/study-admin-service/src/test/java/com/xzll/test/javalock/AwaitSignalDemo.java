package com.xzll.test.javalock;

import com.xzll.test.javajuc.SemaphoreTest;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2023/10/20 10:02
 * @Description:
 */
@Slf4j
public class AwaitSignalDemo {

	private static volatile int shoeCount = 0;
	private static ThreadPoolExecutor producerThread = new ThreadPoolExecutor(1, 1, 1000 * 60, TimeUnit.MILLISECONDS, SemaphoreTest.asyncSenderThreadPoolQueue = new LinkedBlockingQueue<Runnable>(500), new ThreadFactory() {
		private final AtomicInteger threadIndex = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "生产线程_" + this.threadIndex.incrementAndGet());
		}
	});
	private static ThreadPoolExecutor consumerThread = new ThreadPoolExecutor(1, 1, 1000 * 60, TimeUnit.MILLISECONDS, SemaphoreTest.asyncSenderThreadPoolQueue = new LinkedBlockingQueue<Runnable>(500), new ThreadFactory() {
		private final AtomicInteger threadIndex = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "消费线程_" + this.threadIndex.incrementAndGet());
		}
	});

	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		Condition producerCondition = lock.newCondition();
		Condition consumerCondition = lock.newCondition();
		//不停生产鞋，攒够5双了就唤醒消费线程
		producerThread.execute(() -> {
			while (true) {
				lock.lock(); // 获取锁资源
				try {
					if (shoeCount > 5) { //如果生产够5双， 则阻塞等待生产线程，待消费线程消费完后再生产
						System.out.println(Thread.currentThread().getName() + "_生产鞋完成" + (shoeCount - 1) + "双");
						consumerCondition.signal();//唤醒消费鞋子的线程
						producerCondition.await();//挂起生产鞋的线程
					} else {
						shoeCount++;//生产鞋子
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();//释放锁资源
				}
			}
		});
		//不停消费鞋，把鞋消费完了就唤醒生产线程然他继续造
		consumerThread.execute(() -> {
			while (true) {
				lock.lock();//获取锁资源
				try {
					if (shoeCount == 0) {//如果消费完了
						System.out.println(Thread.currentThread().getName() + "_鞋子全部消费完了");
						System.out.println();
						producerCondition.signal(); //消费完鞋子之后，唤醒生产鞋子的线程
						consumerCondition.await(); //挂起消费鞋子的线程，等待生产完后唤醒当前挂起线程
					} else {
						shoeCount--;//消费鞋子
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					lock.unlock();//释放锁资源
				}
			}
		});
	}
}
