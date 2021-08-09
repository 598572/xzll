package com.xzll.test.websocket.handler.impl;

import com.xzll.test.websocket.handler.MessageHandler;
import com.xzll.test.websocket.message.AuthRequest;
import com.xzll.test.websocket.message.AuthResponse;
import com.xzll.test.websocket.message.UserJoinNoticeRequest;

import com.xzll.test.websocket.util.WebSocketUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/31 13:10
 * @Description:
 */
@Component
public class AuthMessageHandler  implements MessageHandler<AuthRequest> {
    @Override
    public void execute(Session session, AuthRequest message) {
        // 如果未传递 accessToken
        if (StringUtils.isEmpty(message.getAccessToken())) {
            WebSocketUtil.send(session, AuthResponse.TYPE,
                    new AuthResponse().setCode(1).setMessage("认证 accessToken 未传入"));
            return;
        }

        // 添加到 WebSocketUtil 中
        WebSocketUtil.addSession(session, message.getAccessToken()); // 考虑到代码简化，我们先直接使用 accessToken 作为 User

        // 判断是否认证成功。这里，假装直接成功
        WebSocketUtil.send(session, AuthResponse.TYPE, new AuthResponse().setCode(0));

        // 通知所有人，某个人加入了。这个是可选逻辑，仅仅是为了演示
        WebSocketUtil.broadcast(UserJoinNoticeRequest.TYPE,
                new UserJoinNoticeRequest().setNickname(message.getAccessToken())); // 考虑到代码简化，我们先直接使用 accessToken 作为 User
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
