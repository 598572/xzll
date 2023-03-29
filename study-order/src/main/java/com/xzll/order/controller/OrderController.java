package com.xzll.consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@RestController
@RequestMapping("/order/test")
@Slf4j
public class OrderController {


    @RequestMapping("/craeteOrder")
    public String craeteOrder() {
        //订单服务 rest接口测试
        return "ok";
    }

}
