package com.xzll.auth.security.service;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 * 客户端信息
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

//    @Autowired
//    private OAuthClientFeignClient oAuthClientFeignClient;

	/**
	 * 查询客户端id和secret去数据库  与前端传过来的做对比
	 * @param clientId
	 * @return
	 */
    @Override
    @SneakyThrows
    public ClientDetails loadClientByClientId(String clientId) {
        try {
//            Result<SysOauthClient> result = oAuthClientFeignClient.getOAuthClientById(clientId);
//            if (Result.success().getCode().equals(result.getCode())) {
//                SysOauthClient client = result.getData();
//                BaseClientDetails clientDetails = new BaseClientDetails(
//                        client.getClientId(),
//                        client.getResourceIds(),
//                        client.getScope(),
//                        client.getAuthorizedGrantTypes(),
//                        client.getAuthorities(),
//                        client.getWebServerRedirectUri());
//                clientDetails.setClientSecret(PasswordEncoderTypeEnum.NOOP.getPrefix() + client.getClientSecret());
            BaseClientDetails baseClientDetails = new BaseClientDetails();
            baseClientDetails.setClientId("123456");
            baseClientDetails.setClientSecret("hzz");


            return baseClientDetails;
//            } else {
//                throw new NoSuchClientException("No client with requested id: " + clientId);
//            }
        } catch (EmptyResultDataAccessException var4) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }
}
