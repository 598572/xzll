package com.xzll.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/12 18:22:30
 * @Description: map构造器
 */
public class MapUtil {

	public static final <K, V> MapBuilder<K, V> mapBuilder() {
		return new MapBuilder<K, V>();
	}

	public static class MapBuilder<K, V> {

		private Map<K, V> map;

		public MapBuilder<K, V> init() {
			map = new HashMap<>();
			return this;
		}

		public MapBuilder<K, V> withEntry(K key, V value) {

			if (map == null) {
				throw new RuntimeException("Map builder is not init!");
			}

			if (map.containsKey(key)) {
				return this;
			}

			map.put(key, value);
			return this;
		}

		public Map<K, V> toMap() {

			if (map == null) {
				throw new RuntimeException("Map builder is not init!");
			}

			return this.map;
		}
	}
}
