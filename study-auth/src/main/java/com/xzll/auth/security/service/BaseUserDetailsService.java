package com.xzll.auth.security.service;

import com.xzll.admin.api.dto.AdminUserDTO;
import com.xzll.admin.api.feign.AdminUserFeignClient;
import com.xzll.auth.entity.UserDetailsWapper;

import com.xzll.common.base.XzllAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


/**
 * 从数据库获取用户信息，用于和前端传过来的用户信息进行密码判读以及权限校验
 */
@Slf4j
@Configuration
public class BaseUserDetailsService implements UserDetailsService {

	/**
	 * admin服务的feign
	 */
	@Autowired
	private AdminUserFeignClient adminUserFeignClient;

    /**
     * 根据username获取登录的user信息 去数据库, 没查到的话需要抛出 找不到用户异常
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (Objects.isNull(attributes)) {
            throw new RuntimeException("当前请求为空");
        }

		HttpServletRequest request = attributes.getRequest();
		String clientId = request.getParameter("client_id");

//		List<AdminUserDTO> byUserName = adminUserFeignClient.findByUserName(username);
//		if (CollectionUtils.isEmpty(byUserName)){
//			throw new XzllAuthException("用户不存在!");
//		}
		//todo 根据userId 进行权限查询与缓存
		AdminUserDTO adminUserDTO1 = new AdminUserDTO();
		adminUserDTO1.setUsername("黄壮壮28198");
		AdminUserDTO adminUserDTO = adminUserDTO1;
//		AdminUserDTO adminUserDTO = byUserName.get(0);
		return UserDetailsWapper.builder().username(adminUserDTO.getUsername())
				.password(adminUserDTO.getPassword()).build();
    }

}
