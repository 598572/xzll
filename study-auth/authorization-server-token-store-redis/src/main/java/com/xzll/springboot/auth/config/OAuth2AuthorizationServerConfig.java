package com.xzll.springboot.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

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
     * Redis 连接的工厂
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


    /**
     * 2.使用redis存储令牌(token)
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(redisTokenStore());//使用redis存储用户名密码及token信息
    }


    /**
     * 设置 /oauth/check_token 端点，通过认证后可访问。
     *
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("isAuthenticated()");//需要认证
//        oauthServer.tokenKeyAccess("isAuthenticated()")
//                .checkTokenAccess("isAuthenticated()");
//        oauthServer.tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()");
    }

    /**
     * 注意 使用redis的时候 客户端信息存不了 至少目前我了解的是这样的 ，后续再看看有别的方式没有
     */
//    @Bean
//    public ClientDetailsService jdbcClientDetailsService() {
//        return new RedisClientDetailsService(redisConnectionFactory); //RedisClientDetailsService 没有这个类
//    }

    /**
     * 1.使用内存进行客户端信息的存储
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.withClientDetails(jdbcClientDetailsService()); 没见到redis是怎么存的 所以这里使用内存
        /**
         * 没见到redis是怎么存的 所以这里还使用内存存储客户端信息
         */
        clients.inMemory()
                .withClient("clientapp").secret("888888") // Client 账号、密码。
                .authorizedGrantTypes("password","refresh_token") // 密码模式
//                .accessTokenValiditySeconds(3600)//设置访问令牌的有效期。 3600s =2 小时
//                .refreshTokenValiditySeconds(86400)//设置refresh token的有效期 为 86400s = 10天
                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
//                .and().withClient() // 可以继续配置新的 Client
        ;
    }

}
