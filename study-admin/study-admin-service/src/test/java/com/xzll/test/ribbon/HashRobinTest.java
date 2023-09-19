package com.xzll.test.ribbon;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 哈希算法
 */
public class HashRobinTest {

	public static void main(String[] args) {
		List<String> collect = Stream.of("100", "200", "300", "400", "500", "100").collect(Collectors.toList());
		collect.forEach(x->{
			System.out.println("当前路由结果: "+hash(x));
		});

	}


	private static List<String> serverList = new ArrayList<>();

	static {
		serverList.add("客服1");
		serverList.add("客服2");
		serverList.add("客服3");
		serverList.add("客服4");

	}

	public static String hash(String clientIP) {
		//获取服务器数量
		int serverCount = serverList.size();
		//如果没有可用的服务器返回null
		if (serverCount == 0) {
			return null;
		}
		//将客户端IP地址进行哈希计算
		int hashCode = clientIP.hashCode();
		//根据哈希值计算需要转发到哪台服务器上
		int serverIndex = hashCode % serverCount;
		//返回对应的服务器地址
		return serverList.get(serverIndex);
	}

}
