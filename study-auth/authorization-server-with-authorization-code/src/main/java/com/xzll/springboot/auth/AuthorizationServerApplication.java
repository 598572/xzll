package com.xzll.springboot.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 授权服务器 --- 基于授权码模式
 *
 * 在浏览器打开该链接
 *
 * http://127.0.0.1:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://127.0.0.1:9090/callback&response_type=code&scope=read_userinfo
 *
 * 选择同意并点击认证 将会重定向到 redirect_uri 的值(callback的uri) 见resources的图片
 *
 * 授权码模式，是功能最完整、流程最严密的授权模式。它的特点就是通过客户端的后台服务器，与授权务器进行互动
 *   一般情况下，在有客户端的情况下，我们与第三方平台常常采用这种方式。
 *   流程:
 *  （A）用户访问客户端，后者将前者跳转到到授权服务器。
 *  （B）用户选择是否给予客户端授权。
 *  （C）假设用户给予授权，授权服务器将跳转到客户端事先指定的”重定向 URI”（Redirection URI），同时附上一个授权码。
 *  （D）客户端收到授权码，附上早先的”重定向 URI”，向认证服务器申请令牌。这一步是在客户端的后台的服务器上完成的，对用户不可见。
 *  （E）认证服务器核对了授权码和重定向 URI，确认无误后，向客户端发送访问令牌。
 *
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
