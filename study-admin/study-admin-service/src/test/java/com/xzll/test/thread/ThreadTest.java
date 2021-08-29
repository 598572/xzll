package com.xzll.test.thread;


/**
 * @Author: hzz
 * @Date: 2021/8/27 13:20:29
 * @Description: 多线程之 sleep yield join方法演示
 */
public class ThreadTest {


	public static void main(String[] args) {
//		testSleep();
		testYield();
//		try {
//			testJoin();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	/*Sleep,意思就是睡眠，当前线程暂停一段时间让给别的线程去运行。Sleep是怎么复活的？由你的睡眠时间而定，等睡眠到规定的时间自动复活*/
	static void testSleep() {
		new Thread(() -> {
			for (int i = 0; i < 100; i++) {
				System.out.println("A" + i);
				try {
					Thread.sleep(500);
					//TimeUnit.Milliseconds.sleep(500)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/*
	Yield,就是当前线程正在执行的时候停止下来进入等待队列
	（就绪状态，CPU依然有可能把这个线程拿出来运行），
	当然，更大的可能性是把原来等待的那些拿出一个来执行，所以yield的意思是我让出一下CPU，后面你们能不能抢到那我不管
	*/
	static void testYield() {
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				System.out.println("A" + i);
				Thread.yield();


			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				System.out.println("------------B" + i);
			}
		}).start();
	}

	/*join， 意思就是在自己当前线程加入你调用Join的线程（），本线程等待。等调用的线程运行完了，自己再去执行。t1和t2两个线程，在t1的某个点上调用了t2.join,它会跑到t2去运行，t1等待t2运行完毕继续t1运行（自己join自己没有意义） */
	private static void testJoin() throws InterruptedException {
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				System.out.println("A" + i);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		new Thread(() -> {
			try {
				//在本线程内，等待t1线程执行完毕再执行本线程，如果在main线程中t1.join,那就是main线程让出cpu,给t1执行
				t1.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < 10; i++) {
				System.out.println("B" + i);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


}
