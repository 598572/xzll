package com.xzll.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author Administrator
 * @Create 2020/12/8 0008 - 18:45
 * @Description 注意 服务发现和提供者的 分组  spring.cloud.nacos.discovery.group 一定要一致 否则找不到服务
 */
@FeignClient(value = "study-test")
public interface FeignTestService {

    @GetMapping(value = "/testController/test")
    String test();

}
