package com.xzll.admin.api.feign;


import com.xzll.admin.api.dto.AdminUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * feign客户端
 */
//注意此处的name 必须和 配置文件 中 spring.application.name的值 一致！ 否则会报错  load balancer does not have available server for client nacos

@FeignClient(name = "study-admin")
public interface AdminUserFeignClient {

	@GetMapping(value = "/adminUser/findByUserName")
	List<AdminUserDTO> findByUserName(@RequestParam String username);

}
