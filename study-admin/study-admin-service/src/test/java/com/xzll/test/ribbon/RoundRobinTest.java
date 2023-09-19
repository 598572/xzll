package com.xzll.test.ribbon;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 轮询算法 ok
 */
public class RoundRobinTest {

	//定义一个全局计数器，每次调用累加
	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	private static List<String> serverList = new ArrayList<>();
	static {
		serverList.add("客服0");
		serverList.add("客服1");
		serverList.add("客服2");
		serverList.add("客服3");

	}

	public static void main(String[] args) {
		List<String> collect = Stream.of("100", "200", "300", "400", "500", "100","89","90","91").collect(Collectors.toList());
		collect.forEach(x->{
			System.out.println("当前路由结果: "+ roundRobin());
		});
	}

	public static String roundRobin() {
		//获取服务器数量
		int serverCount = serverList.size();
		int current = atomicInteger.getAndIncrement();
		//获取当前请求应该转发到哪台服务器
		int currentServerIndex = current % serverCount;
		System.out.println("当前i值:" + current + "，当前index:" + currentServerIndex);
		return serverList.get(currentServerIndex);
	}

}
