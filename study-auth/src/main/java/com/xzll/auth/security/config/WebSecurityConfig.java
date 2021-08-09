package com.xzll.auth.security.config;

import com.xzll.auth.security.filter.PhoneAuthenticationFilter;
import com.xzll.auth.security.handler.PhoneSuccessHandler;
//import com.xzll.auth.security.password.CustomPassword;
import com.xzll.auth.security.provider.PhoneAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * 认证服务器的配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 配置url是否需要认证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 由于使用的是JWT，我们这里不需要csrf
        http.cors().and().csrf().disable()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()
				.addFilterBefore(getPhoneLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers("/verify/**").permitAll().and()
                .authorizeRequests().antMatchers("/phone-login/**").permitAll().and()
                .authorizeRequests().antMatchers("/logout/**").permitAll().and()
                .authorizeRequests().antMatchers("/pub-key/jwt.json").permitAll().and()
                .authorizeRequests().antMatchers("/js/**", "/favicon.ico").permitAll().and()
                .authorizeRequests().antMatchers("/v2/api-docs/**", "/webjars/**", "/swagger-resources/**", "/*.html").permitAll().and()
                //其余所有都需要认证
                .authorizeRequests()
                .anyRequest().authenticated();
    }

	/**
	 * 用户验证
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(phoneAuthenticationProvider());
		//auth.inMemoryAuthentication().withUser("hzz").password("123456").roles("admin");

	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new CustomPassword();
//	}

    /**
     * 如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户 所以这里需要重新实例化bean
     *
     * @return
     * @throws Exception
     */
    @Bean
	@Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	/**
	 * 用户验证
	 */
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(phoneAuthenticationProvider());
//	}
//



	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return super.userDetailsService();
	}

	/**
	 * 授权提供者 (bean)
	 * @return
	 */
	@Bean
	public PhoneAuthenticationProvider phoneAuthenticationProvider(){
		PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
		// 设置userDetailsService
		provider.setUserDetailsService(userDetailsService);
		//provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/**
	 * 手机验证码登陆过滤器 (bean)
	 *
	 * @return
	 */
	@Bean
	public PhoneAuthenticationFilter getPhoneLoginAuthenticationFilter() {
		PhoneAuthenticationFilter filter = new PhoneAuthenticationFilter();
		try {
			filter.setAuthenticationManager(this.authenticationManagerBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		filter.setAuthenticationSuccessHandler(getLoginSuccessAuth());
		//filter.setAuthenticationFailureHandler(getLoginFailure());暂时先不做
		return filter;
	}

	/**
	 * 登录成功后处理器 (bean)
	 * @return
	 */
	@Bean
	public PhoneSuccessHandler getLoginSuccessAuth(){
		PhoneSuccessHandler phoneSuccessHandler = new PhoneSuccessHandler();
		return phoneSuccessHandler;
	}


}
