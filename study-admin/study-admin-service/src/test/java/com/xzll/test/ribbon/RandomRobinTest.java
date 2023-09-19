package com.xzll.test.ribbon;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 随机算法 ok
 */
public class RandomRobinTest {

	public static void main(String[] args) {
		List<String> collect = Stream.of("100", "200", "300", "400", "500", "100").collect(Collectors.toList());
		collect.forEach(x->{
			System.out.println("当前路由结果: "+ random());
		});
	}


	private static List<String> serverList = new ArrayList<>();

	static {
		serverList.add("客服1");
		serverList.add("客服2");
		serverList.add("客服3");
		serverList.add("客服4");
	}

	public static String random() {
		//获取服务器数量
		int serverCount = serverList.size();
		//如果没有可用的服务器返回null
		if (serverCount == 0) {
			return null;
		}
		//生成一个随机数
		int randomIndex = ThreadLocalRandom.current().nextInt(serverCount);
		//返回对应的服务器地址
		return serverList.get(randomIndex);
	}

}
