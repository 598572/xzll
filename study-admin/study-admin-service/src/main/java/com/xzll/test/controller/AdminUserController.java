package com.xzll.test.controller;

import com.xzll.admin.api.dto.AdminUserDTO;
import com.xzll.common.base.Result;

import com.xzll.test.service.AdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@Slf4j
@Api(tags = "用户管理-后台")
@RestController
@RequestMapping("/adminUser")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;

    @GetMapping("/findByUserName")
	@ApiOperation(value = "根据username查找用户", notes = "根据username查找用户")
    public List<AdminUserDTO> findByUserName(@RequestParam(value = "username",required = true) String username) {
		return adminUserService.findByUserName(username);
    }

}
