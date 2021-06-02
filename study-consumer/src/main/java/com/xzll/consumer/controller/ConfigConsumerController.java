package com.xzll.consumer.controller;

import com.alibaba.fastjson.JSON;
import com.xzll.consumer.feign.FeignTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@RestController
@RequestMapping("/feign")
public class ConfigConsumerController {

    @Autowired
    private FeignTestService feignTestService;

    @RequestMapping("/test")
    public String get() {
        System.out.println("呵呵呵");
        String test = feignTestService.test();

        String s = JSON.toJSONString(test);
        System.out.println(s);
        return s;
    }

}
