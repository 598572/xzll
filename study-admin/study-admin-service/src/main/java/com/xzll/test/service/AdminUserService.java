package com.xzll.test.service;


import com.xzll.admin.api.dto.AdminUserDTO;

import java.util.List;

public interface AdminUserService {

	/**
	 * 用于测试链路追踪
	 *
	 * @param username
	 * @return
	 */
	List<AdminUserDTO> findByUserNameForTestTrace(String username);

	/**
	 * 用于测试nginx
	 *
	 * @param username
	 * @return
	 */
	List<AdminUserDTO> findByUserNameForTestNginx(String username);


	/**
	 * 添加人员信息
	 *
	 * @param userDTO
	 * @return
	 */
	int addUser(AdminUserDTO userDTO);


	void batchAddUser(List<AdminUserDTO> users);

	AdminUserDTO findUserByUid(Long id);

	int updateUserByUid(AdminUserDTO userDTO);

	void batchUpdateUser(List<AdminUserDTO> userDTO);

	List<AdminUserDTO> findByUserNameForTestAgent(String username);
}
