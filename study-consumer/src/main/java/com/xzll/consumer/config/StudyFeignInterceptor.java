package com.xzll.consumer.config;

import com.xzll.common.constant.StudyConstant;
import com.xzll.common.util.TraceIdUtil;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Author: hzz
 * @Date: 2023/2/27 18:00:43
 * @Description:
 */
@Slf4j
@Component
public class StudyFeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		//获取调用方服务的traceId并传递给 被调用的服务提供者
		String traceId = TraceIdUtil.getTraceIdByLocal();
		if (StringUtils.isBlank(traceId)) {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attributes != null) {
				HttpServletRequest request = attributes.getRequest();
				Enumeration<String> headerNames = request.getHeaderNames();
				if (headerNames != null) {
					String headerName = null;
					while (headerNames.hasMoreElements()) {
						headerName = headerNames.nextElement();
						if (StudyConstant.TraceConstant.TRACE_ID.equalsIgnoreCase(headerName)) {
							traceId = request.getHeader(headerName);
							requestTemplate.header(StudyConstant.TraceConstant.TRACE_ID, traceId);
							TraceIdUtil.setTraceId(traceId);
						}else {
							String values = request.getHeader(headerName);
							requestTemplate.header(headerName, values);
						}
					}
				}
			}
		}else {
			if (StringUtils.isNotBlank(traceId)) {
				requestTemplate.header(StudyConstant.TraceConstant.TRACE_ID, traceId);
			}
		}
	}

	@Bean
	public Logger.Level feignLoggerLevel() {
		//这里记录所有，根据实际情况选择合适的日志level
		return Logger.Level.FULL;
	}
}
