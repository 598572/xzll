package com.xzll.test.service;


import com.xzll.admin.api.dto.AdminUserDTO;

import java.util.List;

public interface AdminUserService {

	List<AdminUserDTO> findByUserName(String username);
}
