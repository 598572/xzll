package com.xzll.test.agent.attach;

/**
 * @Author: 黄壮壮
 * @Date: 2024/4/23 11:28:11
 * @Description:
 */
public class AttachTarget {
	public static void main(String[] args) throws Exception {
		System.out.println("我是被attach的目标程序，现在【开始】执行");

		while (true){
			Integer i1 = 20;//编译后,此处变为 -> Integer i1 = Integer.valueOf(20); 也就是说会隐式调用Integer.valueOf(int i);方法。
			Integer i2 = 20;//编译后,此处变为 -> Integer i2 = Integer.valueOf(20); 也就是说会隐式调用Integer.valueOf(int i);方法。
			if (i1 == i2) {
				System.out.println("动态（attach）插桩 [前] ：i1 == i2：结果:" + (i1 == i2) + "，// 由于20在缓存 -127-128之间，" +
						"所以此处的比较是IntegerCache数组中 两个相同索引位置的对象的比较，对象自己和自己比较，自然是true");
			}
			System.out.println();
			if (!(i1 == i2)) {
				System.out.println("动态（attach）插桩 [后] i1： == i2：结果:" + (i1 == i2) + "，// 由于此时Integer.valueOf(int i);" +
						"方法被动态加载的agent jar修改（只要经过valueOf都会被new Integer装箱），所以此时比较就是两个不同对象，结果自然是false");
			}
			System.out.println();

			Thread.sleep(5000);
		}
	}
}
