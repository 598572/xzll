package com.xzll.auth.security.provider;

import com.xzll.auth.security.token.PhoneAuthenticationToken;
import com.xzll.common.base.XzllAuthException;
import com.xzll.common.util.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;

import static com.xzll.common.base.constant.RedisConstantKey.AuthConstantRedisKey.phoneVerifyKey;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 15:36
 * @Description: 授权服务 提供者
 */
@Slf4j
public class PhoneAuthenticationProvider implements AuthenticationProvider {


	/**注意这里使用set方式注入 */
	private UserDetailsService userDetailsService;

	@Autowired
	private RedisClient redisClient;

	/**
	 * 通过 PhoneAuthenticationFilter 的 attemptAuthentication 方法后 代表尝试认证成功?，开始对其进行认证
	 *
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		PhoneAuthenticationToken authenticationToken = (PhoneAuthenticationToken) authentication;
		// 获取用户信息
		Object principal = authenticationToken.getPrincipal();
		Object credentials = authenticationToken.getCredentials();

		if (Objects.isNull(principal)) throw new XzllAuthException("手机号必填!");
		if (Objects.isNull(credentials)) throw new XzllAuthException("验证码必填!");

		Object verify = redisClient.get(phoneVerifyKey(principal.toString(), "phone"));
		if (verify == null) throw new XzllAuthException("redis没有验证码!");

		if (Objects.equals(verify.toString(), credentials.toString())) {
			log.info("验证码校验通过! 前台传入的验证码码:{},redis中的验证码:{}", credentials, verify);
		}
		String phone = principal.toString();

		UserDetails user = userDetailsService.loadUserByUsername(phone);
		if (Objects.isNull(user)) throw new InternalAuthenticationServiceException("无效认证");

		PhoneAuthenticationToken authenticationResult = new PhoneAuthenticationToken(user, user.getAuthorities());
		authenticationResult.setDetails(authenticationToken.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// 通过类型进行匹配
		return PhoneAuthenticationToken.class.isAssignableFrom(authentication);
	}

	/**
	 * 注意： 为什么这里使用字段注入呢？
	 * <p>
	 * 不使用构造器注入最主要的原因在于会造成依赖环，因为我们这里注入了 UserDetailsService ，而在使用的时候，
	 * PhoneSuccessHandler 里面也同样注入了 UserDetailsService 而后面我们需要在 安全配置 SecurityConfig 中引入
	 * PhoneAuthenticationSecurityConfig ， UserDetailsService 是在 SecurityConfig 创建的，这个时候就会有一个依赖环的问题了。
	 * 是使用的先呢？还是创建的先？Spring 就不知道了，但是构造器注入是 Bean 初始化的时候给的，那个时候不一定有 UserDetailsService ，
	 * 所以使用字段注入，他会在有的时候自动注入进去
	 *
	 * @param userDetailsService
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}
