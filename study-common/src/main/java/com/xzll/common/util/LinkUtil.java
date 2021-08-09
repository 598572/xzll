package com.xzll.common.util;

public class LinkUtil {

	public static final String link(Object... strs) {
		StringBuilder result = new StringBuilder();
		if (strs == null || strs.length <= 0) {
			return "";
		}

		for (Object str : strs) {
			result.append(str);
		}

		return result.toString();
	}
}
