package com.xzll.test.websocket.message;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/31 12:58
 * @Description:
 */
@Data
@Accessors(chain = true)
public class AuthResponse implements Message{


    public static final String TYPE = "AUTH_RESPONSE";

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;
}
