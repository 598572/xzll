package com.xzll.test.ribbon;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.xzll.test.ribbon.po.SeatDataDTO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 最小连接数  实时接待数最小有限分配，相同则比较当天接待数，再相同则随机选取一个坐席
 */
public class LeastConnectionRobinTest {


	private static List<String> serverList = new ArrayList<>();


	//记录每个服务器的连接数
	private static Map<String, Integer> connectionsMap = new ConcurrentHashMap<>();

	static {
		connectionsMap.put("客服1",10);
		connectionsMap.put("客服2",9);
		connectionsMap.put("客服3",8);
		connectionsMap.put("客服4",5);
		serverList.add("客服1");
		serverList.add("客服2");
		serverList.add("客服3");
		serverList.add("客服4");
	}


	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
//			System.out.println("当前路由结果: "+leastConnections());
//			System.out.println("当前路由结果: "+leastConnectionsV2());
//			System.out.println("******");
//			int j = RandomUtil.randomInt(-3, 3);
//			System.out.println("当前随机值: "+j);
		}



		List<SeatDataDTO> list = new ArrayList<>();

		SeatDataDTO seatDataDTO = new SeatDataDTO();
		seatDataDTO.setCurrentConnectCount(10);
		seatDataDTO.setTodayConnectCount(103);
		seatDataDTO.setSeatNumber("客服1");
		list.add(seatDataDTO);

		SeatDataDTO seatDataDTO2 = new SeatDataDTO();
		seatDataDTO2.setCurrentConnectCount(10);
		seatDataDTO2.setTodayConnectCount(101);
		seatDataDTO2.setSeatNumber("客服2");
		list.add(seatDataDTO2);

		SeatDataDTO seatDataDTO3 = new SeatDataDTO();
		seatDataDTO3.setCurrentConnectCount(2);
		seatDataDTO3.setTodayConnectCount(3);
		seatDataDTO3.setSeatNumber("客服3");
		list.add(seatDataDTO3);

		SeatDataDTO seatDataDTO4 = new SeatDataDTO();
		seatDataDTO4.setCurrentConnectCount(99);
		seatDataDTO4.setTodayConnectCount(188);
		seatDataDTO4.setSeatNumber("客服4");
		list.add(seatDataDTO4);

		SeatDataDTO seatDataDTO5 = new SeatDataDTO();
		seatDataDTO5.setCurrentConnectCount(99);
		seatDataDTO5.setTodayConnectCount(18);
		seatDataDTO5.setSeatNumber("客服5");
		list.add(seatDataDTO5);

		SeatDataDTO seatDataDTO6 = new SeatDataDTO();
		seatDataDTO6.setCurrentConnectCount(99);
		seatDataDTO6.setTodayConnectCount(18);
		seatDataDTO6.setSeatNumber("客服6");
		list.add(seatDataDTO6);

		SeatDataDTO seatDataDTO8 = new SeatDataDTO();
		seatDataDTO8.setCurrentConnectCount(99);
		seatDataDTO8.setTodayConnectCount(18);
		seatDataDTO8.setSeatNumber("客服8");
		list.add(seatDataDTO8);

		SeatDataDTO seatDataDTO7 = new SeatDataDTO();
		seatDataDTO7.setCurrentConnectCount(0);
		seatDataDTO7.setTodayConnectCount(18);
		seatDataDTO7.setSeatNumber("客服7");
		list.add(seatDataDTO7);

		List<SeatDataDTO> collect = list.stream().sorted(Comparator.comparing(SeatDataDTO::getCurrentConnectCount)
				.thenComparing(SeatDataDTO::getTodayConnectCount)
				.thenComparing(a->RandomUtil.randomInt(-3, 3))).collect(Collectors.toList());
		collect.forEach(y-> System.out.println(JSONUtil.toJsonStr(y)));

	}


	public static String leastConnectionsV2() {
		//获取服务器数量
		int serverCount = serverList.size();
		//如果没有可用的服务器返回null
		if (serverCount == 0) {
			return null;
		}


		LinkedHashMap<String, Integer> collect = connectionsMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, k1) -> k,
				LinkedHashMap::new));


		Map.Entry<String, Integer> minConnect = connectionsMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).findFirst().orElse(null);
		if (Objects.nonNull(minConnect)){
			System.out.println("当前最小key值:"+minConnect.getKey());
			System.out.println("当前最小value值:"+minConnect.getValue());
			String key = minConnect.getKey();
			connectionsMap.put(key,connectionsMap.get(key) + 1);
			//返回连接数最少的服务器地址
			return minConnect.getKey();
		}



		return null;

	}


	public static String leastConnections() {
		//获取服务器数量
		int serverCount = serverList.size();
		//如果没有可用的服务器返回null
		if (serverCount == 0) {
			return null;
		}
		//默认选择第一个服务器
		String selectedServerAddress = serverList.get(0);
		//获取第一个服务器的连接数
		int minConnections = connectionsMap.getOrDefault(selectedServerAddress, 0);
		//遍历服务器列表，寻找连接数最少的服务器
		for (int i = 1; i < serverCount; i++) {
			String serverAddress = serverList.get(i);
			int connections = connectionsMap.getOrDefault(serverAddress, 0);
			if (connections < minConnections) {
				selectedServerAddress = serverAddress;
				minConnections = connections;
			}
		}
		connectionsMap.put(selectedServerAddress,connectionsMap.get(selectedServerAddress) - 1);
		//返回连接数最少的服务器地址
		return selectedServerAddress;
	}


}
