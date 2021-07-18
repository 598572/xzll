package com.xzll.auth.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
                .authorizeRequests().antMatchers("/oauth/**").permitAll().and()
                .authorizeRequests().antMatchers("/logout/**").permitAll().and()
                .authorizeRequests().antMatchers("/pub-key/jwt.json").permitAll().and()
                .authorizeRequests().antMatchers("/js/**", "/favicon.ico").permitAll().and()
                .authorizeRequests().antMatchers("/v2/api-docs/**", "/webjars/**", "/swagger-resources/**", "/*.html").permitAll().and()
                //其余所有都需要认证
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    /**
     * 如果不配置SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户 所以这里需要重新实例化bean
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
