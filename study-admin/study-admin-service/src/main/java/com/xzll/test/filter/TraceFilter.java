package com.xzll.test.filter;

import com.xzll.common.util.TraceIdUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: hzz
 * @Date: 2023/02/27 16:38:42
 * @Description: 链路追踪过滤器
 */
@ConditionalOnClass(Filter.class)
public class TraceFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            String traceId = TraceIdUtil.getTraceIdByRequest(request);
            TraceIdUtil.setTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            TraceIdUtil.cleanTraceId();
        }

    }
}
