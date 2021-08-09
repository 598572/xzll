package com.xzll.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 15:39
 * @Description: 授权成功后的处理程序
 */
@Slf4j
@Component
public class PhoneSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private  ClientDetailsService clientDetailsService;
	@Autowired
	private  PasswordEncoder passwordEncoder;
	@Autowired
	private  AuthorizationServerTokenServices authorizationServerTokenServices;
	@Autowired
	private  ObjectMapper objectMapper;

	/**
	 * 认证成功后的操作 大体是 ，获取用户信息 生成token返回前端
	 *
	 * @param request
	 * @param response
	 * @param authentication
	 * @throws IOException
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

		log.info("登录成功！");
		// 1. 获取客户端认证信息
		String clientId = request.getHeader("clientId");
		String clientSecret = request.getHeader("clientSecret");
		if (StringUtils.isBlank(clientId)||StringUtils.isBlank(clientSecret)) {
			throw new UnapprovedClientAuthenticationException("请求头中无客户端信息");
		}

		// 解密请求头
		//String[] client = extractAndDecodeHeader(header);
//		if (client.length != 2) {
//			throw new BadCredentialsException("Invalid basic authentication token");
//		}
//		String clientId = client[0];
//		String clientSecret = client[1];

		// 获取客户端信息进行对比判断
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//		if (clientDetails == null) {
//			throw new UnapprovedClientAuthenticationException("客户端信息不存在：" + clientId);
//		} else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
//			throw new UnapprovedClientAuthenticationException("客户端密钥不匹配" + clientSecret);
//		}
		// 2. 构建令牌请求
		TokenRequest tokenRequest = new TokenRequest(new HashMap<>(0), clientId, clientDetails.getScope(), "custom");
		// 3. 创建 oauth2 令牌请求
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		// 4. 获取当前用户信息（省略，前面已经获取过了）
		// 5. 构建用户授权令牌 (省略，已经传过来了)
		// 6. 构建 oauth2 身份验证令牌
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		// 7. 创建令牌
		OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

		// 直接结束
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(objectMapper.writeValueAsString(accessToken));

	}


	/**
	 * 对请求头进行解密以及解析
	 *
	 * @param header 请求头
	 * @return 客户端信息
	 */
//	private String[] extractAndDecodeHeader(String header) {
//		byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
//		byte[] decoded;
//		try {
//			decoded = Base64.getDecoder().decode(base64Token);
//		} catch (IllegalArgumentException e) {
//			throw new BadCredentialsException(
//					"Failed to decode basic authentication token");
//		}
//		String token = new String(decoded, StandardCharsets.UTF_8);
//		int delimiter = token.indexOf(":");
//
//		if (delimiter == -1) {
//			throw new BadCredentialsException("Invalid basic authentication token");
//		}
//		return new String[]{token.substring(0, delimiter), token.substring(delimiter + 1)};
//	}

}
