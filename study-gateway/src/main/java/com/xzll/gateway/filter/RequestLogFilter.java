package com.xzll.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.xzll.common.util.TraceIdUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @Author: hzz
 * @Date: 2023/2/25 16:48:03
 * @Description: 打印网关日志
 */
@Slf4j
@Component
@AllArgsConstructor
public class RequestLogFilter implements GlobalFilter, Ordered {

	private static final String START_TIME = "startTime";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("================ 网关请求开始  ================");
		Mono<Void> then;
		try {
			//参数打印
			String requestUrl = exchange.getRequest().getURI().getRawPath();
			String requestMethod = exchange.getRequest().getMethodValue();
			HttpHeaders headers = exchange.getRequest().getHeaders();
			Map<String, Object> headerMap = new HashMap<>();
			try {
				headers.forEach((headerName, headerValue) -> {
					headerMap.put(headerName, StringUtils.join(headerValue));
				});
			} catch (Exception exception) {
				log.error("网关解析header出错：", exception);
			}
			log.info("网关请求日志_url:{}, method:{}, headers:{}", requestUrl, requestMethod, JSONUtil.toJsonStr(headerMap));
			log.info("================ 网关请求结束  ================");

			exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
			then = chain.filter(exchange).then(Mono.fromRunnable(() -> {
				ServerHttpResponse response = exchange.getResponse();
				Long startTime = exchange.getAttribute(START_TIME);
				long executeTime = 0L;
				if (startTime != null) {
					executeTime = (System.currentTimeMillis() - startTime);
				}
				// 打印请求头
				HttpHeaders httpHeaders = response.getHeaders();
				Map<String, Object> rpsHeaderMap = new HashMap<>();
				httpHeaders.forEach((headerName, headerValue) -> {
					rpsHeaderMap.put(headerName, StringUtils.join(headerValue));
				});
				log.info("================  网关响应  =================");
				log.info("网关响应日志_url:{},method:{},status:{},execTime:{} ms,headers:{}", requestUrl, requestMethod, (Objects.isNull(response.getStatusCode()) ? StringUtils.EMPTY : response.getStatusCode().value()), executeTime, JSONUtil.toJsonStr(rpsHeaderMap));
			}));
		} finally {
			TraceIdUtil.cleanTraceId();
		}
		return then;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
