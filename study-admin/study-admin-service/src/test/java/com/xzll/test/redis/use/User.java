package com.xzll.test.redis.use;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/8/26 18:15:25
 * @Description:
 */
@Data
public class User {
	private Long id;
	private String name;
	private String address;
	private Integer age;


	//Object转Map
	public static Map<String, Object> getObjectToMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> cla = obj.getClass();
		Field[] fields = cla.getDeclaredFields();
		Long id = null;
		for (Field field : fields) {
			field.setAccessible(true);
			String keyName = field.getName();
			Object value = null;
			try {
				value = field.get(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (value == null)
				value = "";
			if ("id".equals(keyName)) {
				id = Long.parseLong(value.toString());
			}
			map.put(keyName + ":"+id, value);
		}
		return map;
	}

}
