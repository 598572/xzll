package com.xzll.springboot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 授权服务器 --- 基于客户端模式
 *
 * 客户端模式，指客户端以自己的名义，而不是以用户的名义，向授权服务器进行认证。
 * 严格地说，客户端模式并不属于 OAuth 框架所要解决的问题。在这种模式中，用户直接向客户端注册，客户端以自己的名义要求授权服务器提供服务，其实不存在授权问题。
 *
 * 我们对接微信公众号时，就采用的客户端模式。我们的后端服务器就扮演“客户端”的角色，与微信公众号的后端服务器进行交互
 *
 * 流程:
 * （A）客户端向授权服务器进行身份认证，并要求一个访问令牌。
 * （B）授权服务器确认无误后，向客户端提供访问令牌。
 *
 * 搭建授权服务器摘要:
 * 删除 SecurityConfig 配置类，因为客户端模式下，无需 Spring Security 提供用户的认证功能。
 * 但是，Spring Security OAuth 需要一个 PasswordEncoder Bean，否则会报错，因此我们在 OAuth2AuthorizationServerConfig 类的 #passwordEncoder() 方法进行创建。
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
