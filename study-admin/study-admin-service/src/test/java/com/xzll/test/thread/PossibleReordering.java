package com.xzll.test.thread;

/**
 * @Author: hzz
 * @Date: 2021/8/27 16:30:24
 * @Description:
 */
public class PossibleReordering {

	static int x = 0, y = 0;
	static int a = 0, b = 0;

	public static void main(String[] args) throws InterruptedException {
		int i = 0;
		while (true) {
			x = 0;
			y = 0;
			a = 0;
			b = 0;

			i++;
			Thread one = new Thread(new Runnable() {
				public void run() {
					a = 1;
					x = b;
				}
			});
			Thread other = new Thread(new Runnable() {
				public void run() {
					b = 1;
					y = a;
				}
			});
			one.start();
			other.start();
			Thread.sleep(1);//TODO 这里我使用sleep(1)，不对线程进行join,让one和other乱序执行
			//TODO 美团的博客说这里需要one.join()和other.join();我个人在这里持疑问态度
			//TODO 假设one.join();那么肯定是先执行完one里边的run方法在执行other方法。既然one都执行完了，那么a肯定赋值了，也就是说你怎么
			//TODO 重排序，a都是有值的，在执行到other时候，y 肯定 = 1; 也就是说y=1;
			//TODO 我个人认为出现y=0的情况 是因为可见性导致的。也就是说a=1这个操作，在other线程读取变量a时候 a还没有刷新到主内存。所以导致y=0;
			//TODO 不知道我理解的对不对，有时间找个大佬请教下。我在跑程序时候 使用的sleep(1)的方式 就是让这俩程序乱序执行(才可能出现因为重排序引发的y=0现象)

			//TODO 第一步: x = b; (假设one抢到cpu时间片) (并且发生重排序 和 a = 1; 换位置了)
			//TODO 第二步: b = 1; (假设other抢到cpu时间片)
			//TODO 第三步: y = a; (假设other抢到cpu时间片) 注意此时a还没有赋值
			//TODO 第四步: a = 1; (假设one抢到cpu时间片) (并且发生重排序 和 x = b; 换位置了)
//			one.join();
//			other.join();

			String result = "第" + i + "次执行(x=" + x + " y=" + y + ")";
			if (x == 0 && y == 0) {
				System.out.println("第" + i + "次 出现了  : " + "(" + x + "" + "," + y + ")");
				break;
			} else {
				System.out.println(result);
			}
		}
	}

}

//
//	//定义四个静态变量
//	private static int x = 0, y = 0;
//	private static int a = 0, b = 0;
//
//	public static void main(String[] args) throws InterruptedException {
//		int i = 0;
//		while (true) {
//			i++;
//			x = 0;
//			y = 0;
//			a = 0;
//			b = 0;
//			//开两个线程，第一个线程执行a=1;x=b;第二个线程执行b=1;y=a
//			Thread thread1 = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					//线程1会比线程2先执行，因此用nanoTime让线程1等待线程2 0.01毫秒
//					shortWait(10000);
//					a = 1;
//					x = b;
//				}
//			});
//			Thread thread2 = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					b = 1;
//					y = a;
//				}
//			});
//			thread1.start();
//			thread2.start();
//			thread1.join();
//			thread2.join();
//			//等两个线程都执行完毕后拼接结果
//			String result = "第" + i + "次执行x=" + x + "y=" + y;
//			//如果x=0且y=0，则跳出循环
//			if (x == 0 && y == 0) {
//				System.out.println(result);
//				break;
//			} else {
//				System.out.println(result);
//			}
//		}
//	}
//
//	//等待interval纳秒
//	private static void shortWait(long interval) {
//		long start = System.nanoTime();
//		long end;
//		do {
//			end = System.nanoTime();
//		} while (start + interval >= end);
//	}
//}
