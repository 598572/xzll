package com.xzll.test.ribbon;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 加权轮询算法
 */
public class WeightRoundRobinTest {


	//定义一个全局计数器，每次调用累加
	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	//定义服务器列表及服务器权重值
	private static Map<String, Integer> serverMap = new ConcurrentHashMap<>();
	//记录服务器权重总和
	private static int totalWeight = 0;

	static {
		serverMap.put("客服1",2);
		serverMap.put("客服2",2);
		serverMap.put("客服3",5);
		serverMap.put("客服4",3);

	}

	public static void main(String[] args) {
		List<String> collect = Stream.of("100", "200", "300", "400", "500", "100","89","90","91","92","93","94").collect(Collectors.toList());
//		collect.forEach(x->{
//			System.out.println("当前路由结果: "+ weightRoundRobin());
//		});



		Map<String, Integer>  map = new HashMap<>();
		map.put("客服1",2);
		map.put("客服2",2);
		map.put("客服3",5);
		map.put("客服4",3);
		collect.forEach(x->{

			WeightRoundRobinTest weightRoundRobinTest = new WeightRoundRobinTest(map);
			System.out.println("源码->当前路由结果: "+ weightRoundRobinTest.chooseServer());
		});
	}





	public WeightRoundRobinTest(Map<String, Integer> servers) {
		this.servers.addAll(servers.keySet());
		this.serverMap.clear();
		for (String server : servers.keySet()) {
			int weight = servers.get(server);
			serverMap.put(server, weight);
		}
	}


	public static String weightRoundRobin() {
		//获取服务器数量
		int serverCount = serverMap.size();
		//如果没有可用的服务器返回null
		if (serverCount == 0) {
			return null;
		}
		//在此处为避免多线程并发操作造成错误，在方法内部进行锁操作
		synchronized (serverMap) {
			//计算服务器权重总和
			for (Map.Entry<String, Integer> entry : serverMap.entrySet()) {
				totalWeight += entry.getValue();
			}
			//获取当前请求应该转发到哪台服务器
			int currentServerIndex = atomicInteger.incrementAndGet() % totalWeight;
			//遍历服务器列表，根据服务器权重值选择对应地址
			for (Map.Entry<String, Integer> entry : serverMap.entrySet()) {
				String serverAddress = entry.getKey();
				Integer weight = entry.getValue();
				currentServerIndex -= weight;
				if (currentServerIndex < 0) {
					return serverAddress;
				}
			}
		}
		//默认返回null
		return null;
	}





	//----------------------------------------------------------------


	private  List<String> servers = new ArrayList<>();




	private int currentWeightIndex = -1;
	public  synchronized   String chooseServer() {
		int weightSum = serverMap.values().stream().reduce(Integer::sum).orElse(0);

		while (true) {
			currentWeightIndex = (currentWeightIndex + 1) % servers.size();
			String server = servers.get(currentWeightIndex);
			int weight = serverMap.get(server);
			if (weight >= weightSum) {
				return server;
			}
			weightSum -= weight;
		}
	}

}
