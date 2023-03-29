package com.xzll.test.config.http.config;

import com.xzll.common.constant.StudyConstant;
import com.xzll.common.util.TraceIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @Author: hzz
 * @Date: 2023/2/28 15:19:16
 * @Description:
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		String traceId = TraceIdUtil.getTraceIdByLocal();
		if (StringUtils.isNotBlank(traceId)){
			httpRequest.getHeaders().add(StudyConstant.TraceConstant.TRACE_ID, traceId);
		}
		return clientHttpRequestExecution.execute(httpRequest, bytes);
	}
}
