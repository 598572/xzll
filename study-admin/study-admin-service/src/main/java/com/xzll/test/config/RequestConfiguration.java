package com.xzll.test.config;


import com.xzll.test.filter.TraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: hzz
 * @Date: 2023/02/27 16:40:42
 * @Description: 链路追踪过滤器
 */
@Configuration
public class RequestConfiguration {



    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

}
