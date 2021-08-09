package com.xzll.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@RestController
@RequestMapping("/config")
//动态刷新nacos配置 实现热加载
@RefreshScope
@Api(tags = "动态刷新nacos配置 实现热加载-测试")
public class NacosTestController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @GetMapping("/get")
	@ApiOperation(value = "动态刷新nacos配置 实现热加载-测试", notes = "动态刷新nacos配置 实现热加载-测试")
    public boolean get() {
        return useLocalCache;
    }

}
