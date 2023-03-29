package com.xzll.consumer.controller;

import cn.hutool.json.JSONUtil;
import com.xzll.admin.api.dto.AdminUserDTO;
import com.xzll.admin.api.feign.AdminUserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@RestController
@RequestMapping("/consumer/feign")
@Slf4j
public class ConfigConsumerController {

    @Autowired
    private AdminUserFeignClient feignTestService;

    @RequestMapping("/test")
    public String findByUserName() {
        log.info("测试feign接口开始");
		List<AdminUserDTO> rsp = feignTestService.findByUserName("哈哈");
		log.info("测试feign接口结束:{}", JSONUtil.toJsonStr(rsp));
        return "ok";
    }

}
