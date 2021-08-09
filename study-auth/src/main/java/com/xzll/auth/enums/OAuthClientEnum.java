package com.xzll.auth.enums;

import lombok.Getter;

import java.util.Objects;


public enum OAuthClientEnum {
    CLIENT("xzll-client", "客户端"),
    ADMIN("xzll-admin", "系统管理端"),
    WEAPP("xzll-weapp", "微信小程序端"),
    UNKNOWN("unknown", "未知");


    @Getter
    private String clientId;

    @Getter
    private String desc;

    OAuthClientEnum(String clientId, String desc) {
        this.clientId = clientId;
        this.desc = desc;
    }

    public static OAuthClientEnum getByClientId(String clientId) {
        for (OAuthClientEnum client : OAuthClientEnum.values()) {
            if (Objects.equals(client.getClientId(), clientId)) {
                return client;
            }
        }
        return UNKNOWN;
    }
}
