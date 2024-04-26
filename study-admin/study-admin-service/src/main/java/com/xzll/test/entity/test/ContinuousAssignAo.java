package com.xzll.test.entity.test;

import lombok.Data;

/**
 * 连环单 信息
 */
@Data
public class ContinuousAssignAo extends BaseDriverCallBackAo {
    /**
     * 上一笔单终点经度，必填
     */
    private Double preDestLng;

    /**
     * 上一笔单终点纬度，必填
     */
    private Double preDestLat;

    /**
     * 上一笔渠道订单号，当不是同一渠道时，为空
     */
    private String preApOrderId;
    /**
     * 上一笔万顺订单号
     */
    private String preOrderId;

    /**
     * 上一笔单剩余送驾距离,单位米，必填
     */
    private Integer preRemainDistance;

    /**
     * 上一笔单剩余送驾时间,单位秒，必填
     */
    private Integer preRemainSecond;

    /**
     * 当前单接驾距离(算上上一笔单送驾距离),单位米，必填
     */
    private Integer wholePickupDistance;

    /**
     * 当前单接驾时间(算上上一笔单送驾时间),单位秒，必填
     */
    private Integer wholePickupSecond;


    public Double getPreDestLng() {
        return preDestLng;
    }

    public void setPreDestLng(Double preDestLng) {
        this.preDestLng = preDestLng;
    }

    public Double getPreDestLat() {
        return preDestLat;
    }

    public void setPreDestLat(Double preDestLat) {
        this.preDestLat = preDestLat;
    }

    public String getPreApOrderId() {
        return preApOrderId;
    }

    public void setPreApOrderId(String preApOrderId) {
        this.preApOrderId = preApOrderId;
    }

    public String getPreOrderId() {
        return preOrderId;
    }

    public void setPreOrderId(String preOrderId) {
        this.preOrderId = preOrderId;
    }

    public Integer getPreRemainDistance() {
        return preRemainDistance;
    }

    public void setPreRemainDistance(Integer preRemainDistance) {
        this.preRemainDistance = preRemainDistance;
    }

    public Integer getPreRemainSecond() {
        return preRemainSecond;
    }

    public void setPreRemainSecond(Integer preRemainSecond) {
        this.preRemainSecond = preRemainSecond;
    }

    public Integer getWholePickupDistance() {
        return wholePickupDistance;
    }

    public void setWholePickupDistance(Integer wholePickupDistance) {
        this.wholePickupDistance = wholePickupDistance;
    }

    public Integer getWholePickupSecond() {
        return wholePickupSecond;
    }

    public void setWholePickupSecond(Integer wholePickupSecond) {
        this.wholePickupSecond = wholePickupSecond;
    }
}
