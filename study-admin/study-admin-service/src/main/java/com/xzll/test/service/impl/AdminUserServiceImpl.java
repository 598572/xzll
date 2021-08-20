package com.xzll.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.xzll.admin.api.dto.AdminUserDTO;
import com.xzll.test.entity.AdminUserDO;
import com.xzll.test.mapper.AdminUserMapper;
import com.xzll.test.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:32
 * @Description:
 */
@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserMapper adminUserMapper;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdminUserDTO> findByUserName(String username) {
		LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(AdminUserDO::getUsername,username);
		List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(queryWrapper);
		return adminUserDOS.stream().map(adminUserDO->{
			AdminUserDTO adminUserDTO = new AdminUserDTO();
			BeanUtils.copyProperties(adminUserDO,adminUserDTO);
			return adminUserDTO;
		}).collect(Collectors.toList());
	}
}
