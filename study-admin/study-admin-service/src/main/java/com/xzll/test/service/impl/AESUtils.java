//package com.xzll.test.service.impl;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//
///**
// * @Author: hzz
// * @Date: 2024/1/23 15:05:20
// * @Description:
// */
//public class AESUtils {
//	private static Logger logger = LoggerFactory.getLogger(AESUtils.class);
//	private static final String sKey = "23kmkkln8i9i7hc6";
//
//	public AESUtils() {
//	}
//
//	public static String encrypt(String content) {
//		return encrypt(sKey, content);
//	}
//
//	public static String encrypt(String key, String content) {
//		if (StringUtils.isBlank(content)) {
//			return content;
//		} else {
//			Object var2 = null;
//
//			byte[] encrypted;
//			try {
//				byte[] raw = key.getBytes("utf-8");
//				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//				cipher.init(1, skeySpec);
//				encrypted = cipher.doFinal(content.getBytes("utf-8"));
//			} catch (Exception var6) {
//				logger.error("Encrypt exception, ", var6);
//				throw new IllegalArgumentException();
//			}
//
//			return (new Base64()).encodeToString(encrypted);
//		}
//	}
//
//	public static String decrypt(String content) {
//		return decrypt(sKey, content);
//	}
//
//	public static String decrypt(String key, String content) {
//		if (StringUtils.isBlank(content)) {
//			return content;
//		} else {
//			try {
//				byte[] raw = key.getBytes(StandardCharsets.UTF_8);
//				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//				cipher.init(2, skeySpec);
//				byte[] encrypted1 = (new Base64()).decode(content);
//				byte[] original = cipher.doFinal(encrypted1);
//				String originalString = new String(original, StandardCharsets.UTF_8);
//				return originalString;
//			} catch (Exception var8) {
//				logger.error("Decrypt exception, ", var8);
//				throw new IllegalArgumentException();
//			}
//		}
//	}
//
//	public static String decrypt(String sSrc, String sKey, String siv) throws Exception {
//		try {
//			if (sSrc != null && sSrc.length() != 0) {
//				if (sKey == null) {
//					throw new Exception("decrypt key is null");
//				} else if (sKey.length() != 16) {
//					throw new Exception("decrypt key length error");
//				} else {
//					byte[] Decrypt = hexToBytes(sSrc);
//					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//					SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(StandardCharsets.UTF_8), "AES");
//					IvParameterSpec iv = new IvParameterSpec(siv.getBytes(StandardCharsets.UTF_8));
//					cipher.init(2, skeySpec, iv);
//					return new String(cipher.doFinal(Decrypt), StandardCharsets.UTF_8);
//				}
//			} else {
//				return null;
//			}
//		} catch (Exception var7) {
//			throw new Exception("decrypt error", var7);
//		}
//	}
//
//	public static byte[] hexToBytes(String str) {
//		if (str == null) {
//			return null;
//		} else {
//			char[] hex = str.toCharArray();
//			int length = hex.length / 2;
//			byte[] raw = new byte[length];
//
//			for(int i = 0; i < length; ++i) {
//				int high = Character.digit(hex[i * 2], 16);
//				int low = Character.digit(hex[i * 2 + 1], 16);
//				int value = high << 4 | low;
//				if (value > 127) {
//					value -= 256;
//				}
//
//				raw[i] = (byte)value;
//			}
//
//			return raw;
//		}
//	}
//
//	public static String encryptPassenger(String content) {
//		if (StringUtils.isBlank(content)) {
//			return content;
//		} else {
//			Object var1 = null;
//
//			byte[] encrypted;
//			try {
//				byte[] raw = sKey.getBytes("utf-8");
//				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//				cipher.init(1, skeySpec);
//				encrypted = cipher.doFinal(content.getBytes("utf-8"));
//			} catch (Exception var5) {
//				return content;
//			}
//
//			return (new Base64()).encodeToString(encrypted);
//		}
//	}
//
//	public static String decryptPassenger(String content) {
//		if (StringUtils.isBlank(content)) {
//			return content;
//		} else {
//			try {
//				byte[] raw = sKey.getBytes("utf-8");
//				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//				cipher.init(2, skeySpec);
//				byte[] encrypted1 = (new Base64()).decode(content);
//				byte[] original = cipher.doFinal(encrypted1);
//				String originalString = new String(original, "utf-8");
//				return originalString;
//			} catch (Exception var7) {
//				return content;
//			}
//		}
//	}
//
//	public static byte[] EncryptCBC(String src, String key, String iv) {
//		if (key == null) {
//			logger.error("Key为空null");
//			throw new NullPointerException();
//		} else if (key.length() < 32) {
//			logger.error("Key长度小于32位");
//			throw new IllegalArgumentException();
//		} else {
//			try {
//				byte[] raw = key.getBytes("utf-8");
//				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//				IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
//				cipher.init(1, skeySpec, ivParameterSpec);
//				byte[] encrypted = cipher.doFinal(src.getBytes());
//				return encrypted;
//			} catch (Exception var8) {
//				logger.error("数据加密异常,", var8);
//				throw new IllegalArgumentException();
//			}
//		}
//	}
//
//	public static void main(String[] args) throws Exception {
//
//		String encrypt = encrypt("蝎子莱莱爱打怪_2");
//		System.out.println("加密后的字串是：" + encrypt);
//		String decrypt = decrypt(encrypt);
//		System.out.println("解密后的字串是：" + decrypt);
//	}
//
//}
