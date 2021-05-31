package com.xzll.test.websocket.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/31 12:58
 * @Description:
 */
@Data
@Accessors(chain = true)
public class UserJoinNoticeRequest implements Message{

    public static final String TYPE = "USER_JOIN_NOTICE_REQUEST";

    /**
     * 昵称
     */
    private String nickname;
}
