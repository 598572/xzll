package com.xzll.test.controller;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: hzz
 * @Date: 2021/12/22 10:14:00
 * @Description:
 */
public class MD5Util {

	public MD5Util() {
	}

	public static String string2MD5(String inStr) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception var8) {
			var8.printStackTrace();
			return "";
		}

		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for(int i = 0; i < charArray.length; ++i) {
			byteArray[i] = (byte)charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();

		for(int i = 0; i < md5Bytes.length; ++i) {
			int val = md5Bytes[i] & 255;
			if (val < 16) {
				hexValue.append("0");
			}

			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

//	public static String md5Encode(String value, String uniCode) {
//		String tmp = null;
//
//		try {
//			MessageDigest md5 = MessageDigest.getInstance("MD5");
//			md5.update(value.getBytes(uniCode));
//			byte[] md = md5.digest();
//			tmp = binToHex(md);
//		} catch (NoSuchAlgorithmException var5) {
//			var5.printStackTrace();
//		} catch (UnsupportedEncodingException var6) {
//			var6.printStackTrace();
//		}
//
//		return tmp;
//	}
//
//	public static String binToHex(byte[] md) {
//		StringBuffer sb = new StringBuffer();
//		int read = false;
//
//		for(int i = 0; i < md.length; ++i) {
//			int read = md[i];
//			if (read < 0) {
//				read += 256;
//			}
//
//			if (read < 16) {
//				sb.append("0");
//			}
//
//			sb.append(Integer.toHexString(read));
//		}
//
//		return sb.toString();
//	}

	public static int differentDays(Date date1, Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		// 同一年
		if(year1 != year2){
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++){
				//闰年
				if(i%4==0 && i%100!=0 || i%400==0){
					timeDistance += 366;
					//不是闰年
				} else {
					timeDistance += 365;
				}
			}
			return timeDistance + (day2-day1) ;
			// 不同年
		}else {
			return day2-day1;
		}
	}

	public static void main(String[] args) throws InterruptedException {
//		String s = "{1233ewqfeqwf}";
//		String s2 = "{1233ewqfeqwf}";
//		//System.out.println("原始：" + s);
//		System.out.println("MD5后：" + string2MD5(s));
//		System.out.println("MD5后：" + string2MD5(s2));
//		System.out.println("解密的：" + KL(KL(s)));

		AtomicInteger atomicInteger = new AtomicInteger();
		while (true){
//			Thread.sleep(1000);
			System.out.println("处理一些事情!  ： "+atomicInteger.getAndIncrement());
		}
	}

	public static String KL(String inStr) {
		char[] a = inStr.toCharArray();

		for(int i = 0; i < a.length; ++i) {
			a[i] = (char)(a[i] ^ 116);
		}

		String s = new String(a);
		return s;
	}
}
