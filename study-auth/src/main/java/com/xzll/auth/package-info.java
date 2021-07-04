/**
 * 这个模块是 auth演示模块
 *
 * 一共四种授权服务器 演示了3中 简单模式授权服务器和删除token的没演示 参考了 https://www.iocoder.cn/Spring-Security/OAuth2-learning/?self 大佬的文章 谢谢大佬
 *
 * 用户名密码模式:  <artifactId> authorization-server-with-password-credentials </artifactId>
 *
 * 授权码模式:     <artifactId>authorization-server-with-authorization-code</artifactId>
 *
 * 客户端模式:     <artifactId> authorization-server-with-client-credentials </artifactId>
 *
 * token刷新演示:  <artifactId> authorization-server-with-refresh-token </artifactId>
 *
 * 资源服务器:     <artifactId> resource-server </artifactId>
 *
 * ps: postman文件在 resources文件夹下
 *
 *
 * 目前几个授权服务器存在的问题:
 *
 *    采用基于内存的 InMemoryTokenStore，实现访问令牌和刷新令牌的存储。它会存在两个明显的缺点：
 *    1.重启授权服务器时，令牌信息会丢失，导致用户需要重新授权。
 *    2.多个授权服务器时，令牌信息无法共享，导致用户一会授权成功，一会授权失败。
 *    3.学习完Spring Security OAuth 框架。不过 Spring 团队宣布该框架处于 Deprecation 废弃状态。哈哈 那为什么要学习这个？因为需要基础呀
 *      另外: 很少项目会直接采用 Spring Security OAuth 框架，而是自己参考它进行 OAuth2.0 的实现。并且，一般只会实现密码授权模式。
 *
 *
 * 另外 : 关于token存储,客户端信息存储 的几种方式 也演示了 在  authorization-server-token-store-xxx几个服务中
 *
 *
 * 最后 搭建了SSO授权服务器 和 资源服务器(实际中大多都是网关服务因为网关服务需要去授权服务器进行鉴权)
 * 授权服务器:  authorization-server-on-sso
 * 资源服务器:  resource-server-on-sso
 * 具体的代码和配置 见对应的服务即可 ， postman演示见本classpath下 resources下的  登录认证演示.postman_collection.json  文件
 *
 */
package com.xzll.auth;