package com.xzll.test.mianshi;

/**
 * @Author: hzz
 * @Date: 2023/2/10 15:03:29
 * @Description:
 */
public class IntegerCacheTest {

	/**
	 * public static Integer valueOf(int i) {
	 * if (i >= IntegerCache.low && i <= IntegerCache.high)
	 * return IntegerCache.cache[i + (-IntegerCache.low)];
	 * return new Integer(i);
	 * }
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		int i1 = 200;
		Integer i2 = 200;
		System.out.println("i1 == i2：结果:" + (i1 == i2) + "，// int和new出来的Integer (因为200不在(-127-128)区间，所以会new对象 )比较，i2会自动拆箱 所以会变成两个int的比较");

		Integer i3 = 200;//编译后,此处变为 -> Integer i3 = Integer.valueOf(200); 也就是说不会从IntegerCache中获取，而是new了一个对象
		Integer i4 = 200;
		System.out.println("i3 == i4：结果:" + (i3 == i4) + "，// 由于200不在缓存 -127-128之间，所以此处是两个对象的比较，i3内存地址:" + System.identityHashCode(i3) + " ,i4内存地址:" + System.identityHashCode(i4));


		Integer i15 = new Integer(100);
		Integer i16 = new Integer(100);
		System.out.println("i15 == i16：结果:" + (i15 == i16) + "，// i15和i16是new的两个对象，i15内存地址:" + System.identityHashCode(i15) + " ,i16内存地址:" + System.identityHashCode(i16));

		Integer i17 = 100;
		System.out.println("i15 == i17：结果:" + (i15 == i17) + "，// 由于 i15是new的对象，i17是从Integer缓存数组中拿的对象，两个都是对象，i15内存地址："+System.identityHashCode(i15)+",i17内存地址："+System.identityHashCode(i17));

		Integer i18 = 100;
		System.out.println("i17 == i18："+"结果:" + (i17 == i18) + "，// 由于100 在缓存 -127-128之间，所以此处是两个相同对象的比较（因为都是从数组中拿的同一个index位置的Integer对象），i17内存地址:" + System.identityHashCode(i17) + " ,i18内存地址:" + System.identityHashCode(i18));

		int i100=100;
		System.out.println("i5==i100：结果:"+(i15==i100)+"，// 由于i100是基础类型，和从Integer缓存中拿出来的i17（Integer对象比较）比较，i17会自动拆箱，结果自然是true");
	}
}
