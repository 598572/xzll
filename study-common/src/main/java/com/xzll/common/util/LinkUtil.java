package com.xzll.common.util;

import java.util.concurrent.CompletableFuture;


public class LinkUtil {

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


	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			int finalI = i;
			CompletableFuture.runAsync(()->{
				System.out.println("当前i值："+ finalI);
			});
		}
		Thread.sleep(300000);
	}
}
