package com.xzll.test.websocket.message;

import lombok.Data;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/31 12:58
 * @Description:
 */
@Data
public class SendToOneRequest implements Message{

    public static final String TYPE = "SEND_TO_ONE_REQUEST";

    /**
     * 发送给的用户
     */
    private String toUser;
    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;
}
