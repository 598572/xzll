package com.xzll.auth.security.service;

import com.xzll.auth.entity.OAuthUserDetails;
import com.xzll.auth.enums.OAuthClientEnum;
import com.xzll.auth.result.ResultCode;
import com.xzll.auth.util.JwtUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 从数据库获取用户信息，用于和前端传过来的用户信息进行密码判读以及权限校验
 */
@Configuration
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 根据username获取登录的user信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("当前请求为空");
        }
        String clientId = JwtUtils.getAuthClientId();
        OAuthClientEnum client = OAuthClientEnum.getByClientId(clientId);

//        StudyUserDetails result;
//        OAuthUserDetails oauthUserDetails = null;
//        switch (client) {
//            default:
//
//                //从业务系统获取用户信息
//                result = userFeignClient.getUserByUsername(username);
//
//                if (ResultCode.SUCCESS.getCode().equals(result.getCode())) {
//                    SysUser sysUser = (SysUser) result.getData();
//                    oauthUserDetails = new OAuthUserDetails(sysUser);
//                }
//                break;
//        }
//        if (oauthUserDetails == null || oauthUserDetails.getId() == null) {
//            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
//        } else if (!oauthUserDetails.isEnabled()) {
//            throw new DisabledException("该账户已被禁用!");
//        } else if (!oauthUserDetails.isAccountNonLocked()) {
//            throw new LockedException("该账号已被锁定!");
//        } else if (!oauthUserDetails.isAccountNonExpired()) {
//            throw new AccountExpiredException("该账号已过期!");
//        }

        return new OAuthUserDetails();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes();
//        if (attributes == null) {
//            throw new AuthException("获取不到当先请求");
//        }
//        HttpServletRequest request = attributes.getRequest();
//        String clientId = request.getParameter("client_id");
//        Employee employee = getUser(username, clientId);
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        //查询角色列表
//        List<Role> roles = roleService.getByEmpId(employee.getId());
//        roles.forEach(role -> {
//            //只存储角色，所以不需要做区别判断
//            authorities.add(new SimpleGrantedAuthority(role.getCode()));
//
//        });
//        //处理权限集合
//        dealPermission(employee.getId());
//        // 返回带有用户权限信息的User
//        org.springframework.security.core.userdetails.User user =
//                new org.springframework.security.core.userdetails.User(
//                        employee.getUsername(), employee.getPw(),
//                        isActive(Integer.parseInt(employee.getStatus())),
//                        true,
//                        true,
//                        true, authorities);
//        BaseEmployee baseUser = new BaseEmployee();
//        BeanUtils.copyProperties(employee, baseUser);
//        return new BaseUserDetail(baseUser, user);
//    }


}
