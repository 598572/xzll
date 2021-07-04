package com.xzll.springboot.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * 授权服务器配置
 */
@Configuration
@EnableAuthorizationServer//开启授权服务器的功能
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 用户认证 Manager 在的SecurityConfig类的 authenticationManagerBean方法创建的bean
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 用户详情 Service
     *
     * 如果不进行 UserDetailsService 的设置，在使用刷新令牌获取新的访问令牌时，会抛出异常。
     *
     */
    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 配置使用的 AuthenticationManager 实现用户认证的功能
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * 设置 /oauth/check_token 端点，通过认证后可访问。
     *
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("isAuthenticated()");
//        oauthServer.tokenKeyAccess("isAuthenticated()")
//                .checkTokenAccess("isAuthenticated()");
//        oauthServer.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("clientapp").secret("888888") // Client 账号、密码。
                .authorizedGrantTypes("password", "refresh_token") // 密码模式 (别的模式的token刷新 都是一个原理 就不演示了)， 支持token刷新
                .accessTokenValiditySeconds(3600)//设置访问令牌的有效期。 3600s =2 小时
                .refreshTokenValiditySeconds(86400)//设置refresh token的有效期 为 86400s = 10天
                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
//                .and().withClient() // 可以继续配置新的 Client
        ;
    }

}
