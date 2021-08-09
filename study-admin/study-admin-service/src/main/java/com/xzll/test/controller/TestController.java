package com.xzll.test.controller;

import com.xzll.common.base.Result;
import com.xzll.test.entity.TestEntity;
import com.xzll.test.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@Slf4j
@Api(tags = "测试controller")
@RestController
@RequestMapping("/testController")
public class TestController {

	@Autowired
	private TestService testService;

    @GetMapping("/test")
	@ApiOperation(value = "测试第一个接口查询", notes = "测试第一个接口查询")
    public Result<List<TestEntity>> test(@RequestParam(value = "param",required = true) String param) {
    	log.info("测试接口");
		return Result.createOK(testService.testMybatiesPlus(param));
    }

}
