package com.xzll.test.strategy.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: hzz
 * @Date: 2021/9/12 19:20:30
 * @Description: 退款状态枚举类
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RefundStatusEnum {
    REFUND_NO_AUDIT(0,"退款中"),
    REFUND_SUCESS(1,"退款成功"),
    REFUND_REFUSE(2,"退款驳回");

    private Integer status;
    private String desc;

    public static RefundStatusEnum getRefundByStatus(Integer status) {
        RefundStatusEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            RefundStatusEnum value = values[i];
            if (status ==  value.status) {
                return value;
            }
        }
        return null;
    }

    public int index() {
        return this.ordinal();
    }

}
