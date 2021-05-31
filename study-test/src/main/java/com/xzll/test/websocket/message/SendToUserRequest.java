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
public class SendToUserRequest implements Message{

    public static final String TYPE = "SEND_TO_USER_REQUEST";

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;
}
