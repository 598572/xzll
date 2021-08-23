package com.xzll.test.point;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerPoint implements CommandLineRunner {
	@Override
	public void run(String... args) throws Exception {
		System.out.println("-----------------------[CommandLineRunnerPoint]  扩展点演示 # run  开始--------------------------------------");
		System.out.println("[CommandLineRunnerPoint] # run ; "+"时机：此时已经刷新容器处于run方法的后半部分了 接下来run方法将发布running事件");
		System.out.println("-----------------------[CommandLineRunnerPoint]  扩展点演示 # run  结束--------------------------------------");
		System.out.println();
	}
}
