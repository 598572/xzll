package com.xzll.test.point.aware;

import cn.hutool.core.util.RandomUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TestAgent2 {

	public static final String link(Object... strs) {
		StringBuilder result = new StringBuilder();
		if (strs == null || strs.length <= 0) {
			return "";
		}

		for (Object str : strs) {
			result.append(str);
		}

		return result.toString();
	}


	public static void main(String[] args) throws InterruptedException, ParseException {

		int i = RandomUtil.randomInt(1, 25);
		System.out.println(i);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");




		for (int j = 0; j < 100; j++) {

//			int i1 = RandomUtil.randomInt(0, 2);
//			System.out.println(i1);


			String s = String.valueOf(RandomUtil.randomLong(15028300102L, 19028300102L));
			System.out.println(s);


//			long l = RandomUtil.randomLong(1640967935000L, 1677601535000L);
//
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String sd = sdf.format(new Date(Long.parseLong(String.valueOf(l))));
//			System.out.println(sd);
		}


		Date date = new Date();

//		RandomUtil.randomDate()
//		for (int i = 0; i < 10; i++) {
//			int finalI = i;
//			CompletableFuture.runAsync(()->{
//				System.out.println("当前i值："+ finalI);
//			});
//		}
//		Thread.sleep(300000);
	}




}
