package com.xzll.common.util;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzll.common.rabbitmq.eneity.MqMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/23 11:29:59
 * @Description:
 */
public class JsonFormat {


//	public static void main(String[] args) {
//		JSONObject jo = new JSONObject();
//
//		jo.put("name", "haha");
//		jo.put("age", 18);
//		String formatjosn = formatjosn(jo);
//
//
//	}

	public static String formatjosn(Object json) {
		if (json == null) return "";
		String jsonStr = null;
		HashMap<Object, Object> map = new HashMap<>();
		ObjectMapper om = new ObjectMapper();
		map.put("JSON", json);
		try {
			jsonStr = om.writeValueAsString(map.get("JSON"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();

		}

		System.out.println("转义前: " + json.toString());
		System.out.println("转义后: " + jsonStr);
		return jsonStr;
	}

//	public static void main(String[] args) {
//
////		int i = DateUtil.thisHour(true) ;//* 3600
////		System.out.println(i);
////		int i1 = DateUtil.thisMinute() ;//* 60
////		System.out.println(i1);
////
////		int i2 = DateUtil.thisSecond();
////		System.out.println(i2);
//
//		String substring = "130732".substring(0, 4);
//		System.out.println("sub："+substring);
//
//		Integer nowDate = DateUtil.thisHour(true) * 3600 + DateUtil.thisMinute() * 60 + DateUtil.thisSecond();
//		System.out.println(nowDate);
//		System.out.println("---------");
//		int second = LocalTime.now().getHour()*3600+LocalTime.now().getMinute()*60+LocalTime.now().getSecond();
//		System.out.println(second);
//	}

//	public static void main(String[] args) {
//
//		ArrayList<MqMessage> objects = new ArrayList<>();
//		MqMessage mqMessage = new MqMessage();
//		mqMessage.setExpiration(null);
//		objects.add(mqMessage);
//		MqMessage mqMessage2 = new MqMessage();
//		mqMessage2.setExpiration("123");
//		objects.add(mqMessage2);
//
//		MqMessage mqMessage3 = new MqMessage();
//		mqMessage3.setExpiration("456");
//		objects.add(mqMessage3);
//
//		MqMessage mqMessage4 = new MqMessage();
//		mqMessage4.setExpiration("789");
//		objects.add(mqMessage4);
//
////		List<MqMessage> collect0 = objects.stream()
////				.sorted(Comparator.comparing(MqMessage::getExpiration)).collect(Collectors.toList());
//		List<MqMessage> collect = objects.stream()
//				.sorted(Comparator.comparing(MqMessage::getExpiration, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
//
////		System.out.println(collect0);
//		collect.stream().forEach(e->{
//			String expiration = e.getExpiration();
//			System.out.println(expiration);
//		});
//
//	}


	public static void main(String[] args) {
		MqMessage mqException = new MqMessage();
		mqException.setId("1234");
		mqException.setExpiration(null);

		Map<String, Object> stringObjectMap = BeanUtil.beanToMap(mqException,false,true);

		System.out.println(stringObjectMap);

	}

}
