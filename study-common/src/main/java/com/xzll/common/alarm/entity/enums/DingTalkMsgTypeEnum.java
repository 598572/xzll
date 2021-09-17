package com.xzll.common.alarm.entity.enums;

/**
 * 钉钉提醒方式
 */
public enum DingTalkMsgTypeEnum {
    TEXT("text"),
    LINK("link"),
    MARKDOWN("markdown");

    private String value;

    public String getValue() {
        return this.value;
    }

    private DingTalkMsgTypeEnum(final String value) {
        this.value = value;
    }
}
