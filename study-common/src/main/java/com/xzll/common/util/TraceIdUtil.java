package com.xzll.common.util;

import com.xzll.common.constant.StudyConstant;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: hzz
 * @Date: 2023/2/26 15:19:36
 * @Description:
 */
public class TraceIdUtil {

	public static final String REGEX = "-";

	public static final Object ipLock = new Object();

	public static String ip = NetUtils.getOneInnerIp();

	/**
	 * 从header和参数中获取traceId
	 * 从网关传入数据
	 *
	 * @param request 　HttpServletRequest
	 * @return traceId
	 */
	public static String getTraceIdByRequest(HttpServletRequest request) {
		String traceId = request.getParameter(StudyConstant.TraceConstant.TRACE_ID);
		if (StringUtils.isBlank(traceId)) {
			traceId = request.getHeader(StudyConstant.TraceConstant.TRACE_ID);
		}
		return traceId;
	}

	public static String getTraceIdByLocal() {
		return MDC.get(StudyConstant.TraceConstant.TRACE_ID);
	}

	/**
	 * 传递traceId至MDC
	 *
	 * @param traceId 　链路id
	 */
	public static void setTraceId(String traceId) {
		if (StringUtil.isNotBlank(traceId)) {
			MDC.put(StudyConstant.TraceConstant.TRACE_ID, traceId);
		}
	}

	/**
	 * 构建traceId
	 * @return
	 */
	public static String buildTraceId() {
		return UUID.randomUUID().toString().replaceAll(REGEX, StringUtils.EMPTY);
	}

	/**
	 * 清理traceId
	 */
	public static void cleanTraceId() {
		MDC.clear();
	}


	/**
	 * 获取内网ip
	 *
	 * @return
	 */
	public static String getIp() {
		if (null != ip) {
			return ip;
		}

		synchronized (ipLock) {
			if (null != ip) {
				return ip;
			}

			ip = NetUtils.getOneInnerIp();
			return ip;
		}
	}

}
