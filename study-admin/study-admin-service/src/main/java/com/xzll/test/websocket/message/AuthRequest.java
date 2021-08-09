package com.xzll.test.websocket.message;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/31 12:58
 * @Description:
 */
@Data
@Accessors(chain = true)
public class AuthRequest implements Message{

    public static final String TYPE = "AUTH_REQUEST";

    /**
     * 认证 Token
     */
    private String accessToken;
}
