package com.xzll.agent.config.util;

import java.util.concurrent.CompletableFuture;


public class AgentTest {



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
