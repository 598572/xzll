package com.xzll.gateway.filter;

import com.xzll.common.constant.StudyConstant;
import com.xzll.common.util.TraceIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * @Author: hzz
 * @Date: 2023/2/25 16:48:03
 * @Description: 给请求增加IP地址和TraceId
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PreRequestFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		//traceId生成
		String traceId = TraceIdUtil.buildTraceId();
		//为当前网关服务添加traceId
		TraceIdUtil.setTraceId(traceId);

		//将traceId传递到下游
		ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
				.headers(h -> h.add(StudyConstant.TraceConstant.TRACE_ID, traceId))
				.build();
		ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
		return chain.filter(build);
	}

	/**
	 * 指定该过滤器优先级最高
	 *
	 * @return
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
