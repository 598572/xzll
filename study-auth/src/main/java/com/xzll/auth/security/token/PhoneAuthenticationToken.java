package com.xzll.auth.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 15:19
 * @Description: 自定义token请求对象(我理解他为token的载体)   模仿 UsernamePasswordAuthenticationToken类
 */
public class PhoneAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -7965894132532203173L;

	private final Object principal;
	private Object credentials;

	public PhoneAuthenticationToken(Object principal, Object credentials) {
		super((Collection)null);
		this.principal = principal;
		this.credentials = credentials;
		this.setAuthenticated(false);
	}

	public PhoneAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		} else {
			super.setAuthenticated(false);
		}
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.credentials = null;
	}

}
