package com.xzll.resourceserver.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;

/**
 * SSO 配置 在类上添加 @EnableOAuth2Sso 注解，声明基于 Spring Security OAuth 的方式接入 SSO 功能。
 *
 * 推荐看 SsoSecurityConfigurer 类
 */
@Configuration
@EnableOAuth2Sso
public class OAuthSsoConfig {

}
