package com.xzll.common.base.constant;

import com.xzll.common.base.XzllBaseException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 11:45
 * @Description:
 */
public class RedisConstantKey {

	/**
	 * 认证类相关的redis key
	 */
	public static class AuthConstantRedisKey{

		public static final String KEY_LINK = ":";

		public static String phoneVerifyKey(String param, String type) {
			if (StringUtils.isEmpty(param)) {
				throw new XzllBaseException("请求中缺少手机号或者邮箱号");
			}
			return "code:" + type + KEY_LINK + param;
		}
	}
}
