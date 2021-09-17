package com.xzll.common.alarm.entity.dto;

import com.xzll.common.alarm.entity.enums.DingTalkMsgTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.util.List;


/**
 * @Author: hzz
 * @Date: 2021/9/16 18:44:36
 * @Description: 钉钉消息DTO
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DingTalkRobotBeanDTO implements Serializable {

    private String secret;
    private String accessToken;
    private DingTalkMsgTypeEnum dingTalkMsgTypeEnum;
    private List<String> atMobiles;
    private boolean atAll;
    private String content;
    private String title;
    private String messageUrl;
    private String picUrl;

}
