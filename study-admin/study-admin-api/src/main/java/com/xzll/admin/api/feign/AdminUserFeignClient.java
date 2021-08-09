package com.xzll.admin.api.feign;


import com.xzll.admin.api.dto.AdminUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "study-test")
public interface AdminUserFeignClient {

	@GetMapping(value = "/adminUser/findByUserName")
	List<AdminUserDTO> findByUserName(@RequestParam String username);

}
