package com.xzll.auth.security.filter;

import com.alibaba.nacos.common.utils.HttpMethod;
import com.xzll.auth.security.token.PhoneAuthenticationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 15:02
 * @Description: 手机验证码登录 (授权过滤器)
 *
 *
 * 1.继承 AbstractAuthenticationProcessingFilter 的过滤器
 *
 * 2.继承 AbstractAuthenticationToken 的令牌请求
 *
 * 3.实现 AuthenticationProvider 的授权提供者
 *
 * 4.继承 AbstractAuthenticationToken 的成功处理器
 *
 * 5.配置过滤器、成功处理器等
 *
 *
 *
 */
public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	/**手机号登录 url*/
	private static final String SPRING_SECURITY_PHONE_VERIFY_LOGIN_URL = "/phone-login";
	private static final String SPRING_SECURITY_PHONE_VERIFY_PHONE = "phone";
	private static final String SPRING_SECURITY_PHONE_VERIFY_CODE = "verifyCode";

	public PhoneAuthenticationFilter() {
		// 需要拦截的路径 即登录url
		super(new AntPathRequestMatcher(SPRING_SECURITY_PHONE_VERIFY_LOGIN_URL, HttpMethod.POST));
	}

	/**
	 * 尝试进行认证 (根据手机号和验证码)
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {
		if (!HttpMethod.POST.matches(request.getMethod())) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		// 获取传入的手机号和验证码
		String phone = getParamByRequest(request,SPRING_SECURITY_PHONE_VERIFY_PHONE);
		String verifyCode = getParamByRequest(request,SPRING_SECURITY_PHONE_VERIFY_CODE);
		System.out.println("****attemptAuthentication***一次请求会产生两次认证****:"+request.getServletPath());

		// 在此我们需要创建我们自己的授权 token 其实就是模仿的 UsernamePasswordAuthenticationToken类 呵呵
		PhoneAuthenticationToken authRequest = new PhoneAuthenticationToken(phone,verifyCode);
		setDetails(request, authRequest);
		// 授权管理器对请求进行授权
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * 获取请求中的 手机号和验证码
	 *
	 * @param request 正在为其创建身份验证请求
	 * @return 请求中的 phone 值
	 */
	private String getParamByRequest(HttpServletRequest request, String param) {
		return request.getParameter(param);
	}

	/**
	 * 提供以便子类可以配置放入 authentication request 的 details 属性的内容
	 *
	 * @param request     正在为其创建身份验证请求
	 * @param authRequest 应设置其详细信息的身份验证请求对象
	 */
	private void setDetails(HttpServletRequest request,
							PhoneAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

}
