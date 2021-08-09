package com.xzll.auth.security.config;

import cn.hutool.core.collection.CollectionUtil;
import com.xzll.auth.entity.UserDetailsWapper;
import com.xzll.auth.security.service.ClientDetailsServiceImpl;
import com.xzll.auth.security.service.BaseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 认证授权配置 注意这是授权服务器的核心配置 同时还有WebSecurityConfig也是核心
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;
    //用户信息
    @Autowired
    private BaseUserDetailsService userDetailsService;
    //客户端信息 一般指资源服务器，个人理解
    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;
//    @Autowired
//    private DataSource dataSource;

    /**
     * 使用数据源实例化 jdbcClientDetailsService bean
     *
     * @return
     */
//    @Bean("jdbcClientDetailsService")
//    public ClientDetailsService clientDetailsService() {
//        return new JdbcClientDetailsService(dataSource);
//    }

    /**
     * OAuth2客户端 从数据库加载
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 使用 数据库 作为客户端信息的载体
        clients.withClientDetails(clientDetailsService);

    }


    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services) 不包括端点安全配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService)
                // refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                //      1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                //      2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                .reuseRefreshTokens(true);
    }


    /**
     * 使用非对称加密(RSA)算法对token签名
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥) 并实例化  私钥将会保留在认证服务器上，公钥会对外开放
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        KeyPair keyPair = factory.getKeyPair("jwt", "123456".toCharArray());
        return keyPair;
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = CollectionUtil.newHashMap();
            //从OAuth2Authentication获取用户信息
            UserDetailsWapper userDetailsWapper = (UserDetailsWapper) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("userId", userDetailsWapper.getId());
            additionalInfo.put("username", userDetailsWapper.getUsername());
            //对token内容进行增强 使其携带userName和userId
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * 进行认证
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(userDetailsService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * 密码编码器
     * <p>
     * 委托方式，根据密码的前缀选择对应的encoder，例如：{bcypt}前缀->标识BCYPT算法加密；{noop}->标识不使用任何加密即明文的方式
     * 密码判读 DaoAuthenticationProvider#additionalAuthenticationChecks
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
