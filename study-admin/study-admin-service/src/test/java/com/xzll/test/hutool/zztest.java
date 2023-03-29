package com.xzll.test.hutool;

import java.util.regex.Pattern;

/**
 * @Author: hzz
 * @Date: 2022/11/11 09:38:26
 * @Description:
 */
public class zztest {

	/*
	 * IP地址的匹配标达式 ( // \\d{1,3}) // :\d // 0~9数字，{1,3} // 至少一位，最多三位)
	 */
	private static String regex_IP = "^(121.15.215.(\\d{1,3}))$";
	/*
	 * 字符串 模糊匹配 ：^(.*张三.*name.*)$ ; 等值匹配 ^(张三)$
	 */
	private static String regex_containStr = "^(.*张三.*name.*)$";
	/*
	 * 字符不包含特定字符串的表达式
	 */
	private static String regex_notcontainStr = "^(?!.*(转发)).*$";// 不包含特定字符串的表达式
	public static void main(String[] args) {
		//System.out.println(StringMatchRule("这个邮件 是转发的！", regex_notcontainStr));
//		boolean z = StringMatchRule("张三是", regex_containStr);
//		System.out.println(z);

		String keyword = "马密";

		String contentOne = "占悍马密码"; // LIKE 匹配

		int i = contentOne.indexOf(keyword);
		System.out.println(i);
	}
	public static boolean StringMatchRule(String souce, String regex) {
		boolean result = false;
		if (regex != null && souce != null) {
			result = Pattern.matches(regex, souce);
		}
		return result;
	}



}
