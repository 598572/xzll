package com.xzll.common.http;

import com.xzll.common.constant.StudyConstant;
import com.xzll.common.util.TraceIdUtil;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @Author: hzz
 * @Date: 2023/2/28 15:11:01
 * @Description:
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {

	@Override
	public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
		String traceId = TraceIdUtil.getTraceIdByLocal();
		//当前线程调用中有traceId，则将该traceId进行透传
		if (org.apache.commons.lang3.StringUtils.isNotBlank(traceId)) {
			//为请求头添加traceId 保证传递到 被调用方
			httpRequest.addHeader(StudyConstant.TraceConstant.TRACE_ID, traceId);
			String hostname = TraceIdUtil.getIp();
			if (null != hostname) {
				//为请求头添加ip信息
				httpRequest.addHeader(StudyConstant.TraceConstant.TRACE_ID_HTTP, hostname);
			}
		}
	}


}
