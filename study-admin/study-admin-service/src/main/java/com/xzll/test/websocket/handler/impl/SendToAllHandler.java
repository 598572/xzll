package com.xzll.test.websocket.handler.impl;

import com.xzll.test.websocket.handler.MessageHandler;
import com.xzll.test.websocket.message.SendResponse;
import com.xzll.test.websocket.message.SendToAllRequest;
import com.xzll.test.websocket.message.SendToOneRequest;
import com.xzll.test.websocket.message.SendToUserRequest;
import com.xzll.test.websocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/31 13:12
 * @Description:
 */
@Component
public class SendToAllHandler implements MessageHandler<SendToAllRequest> {
    @Override
    public void execute(Session session, SendToAllRequest message) {
        // 这里，假装直接成功
        SendResponse sendResponse = new SendResponse().setMsgId(message.getMsgId()).setCode(0);
        WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);

        // 创建转发的消息
        SendToUserRequest sendToUserRequest = new SendToUserRequest().setMsgId(message.getMsgId())
                .setContent(message.getContent());
        // 广播发送
        WebSocketUtil.broadcast(SendToUserRequest.TYPE, sendToUserRequest);
    }

    @Override
    public String getType() {
        return SendToAllRequest.TYPE;
    }
}
