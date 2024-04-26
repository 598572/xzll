package com.xzll.test.entity.test;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 */
@Data
public class AcceptingCallbackArgAo extends BaseDriverCallBackAo {


    private String rideChannelSubOrderId;


    @NotNull
    private String driverId;


    @NotNull
    private String name;


    @NotNull
    private String mobile;


    @NotNull
    private String modelName;


    @NotNull
    private String groupName;


    @NotNull
    private String vehicleColor;


    private String vehiclePic;

    /**
     * 必选, 车牌号码
     */
    @NotNull
    private String licensePlates;

    /**
     * 必选, 司机头像链接
     */
    @NotNull
    private String photoSrc;

    /**
     * 必选, 司机星级
     */
    @NotNull
    private String driverRate;

    /**
     * 可选, 车型
     */
    private Integer groupId;
    /**
     * 运力类型
     */
    private Integer rideType;

    /**
     * 可选, 司机纬度，需要单独配置
     */
    private String driverLongitude;

    /**
     * 可选, 司机经度，需要单独配置
     */
    private String driverLatitude;

    /**
     * 可选, 司机接驾距离
     */
    private Integer pickDistance;

    /**
     * 可选, 司机接驾时间
     */
    private Integer pickEstimateTime;

    /**
     * 连环单信息
     */
    private ContinuousAssignAo continuousAssignAo;
}
