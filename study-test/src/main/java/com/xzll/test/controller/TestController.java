package com.xzll.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
//@Slf4j
@RestController
@RequestMapping("/testController")
public class TestController {

    @GetMapping("/test")
    public String test() {
//        log.info("hahhaha");
        return "test";
    }

}
