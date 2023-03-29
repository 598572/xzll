package com.xzll.test.controller;

import lombok.Data;

import java.util.Date;

/**
 * @Author: hzz
 * @Date: 2021/11/10 17:42:01
 * @Description:
 */
@Data
public class DjOrderServiceDrivingorderStationDaily {
	private Long	id;
	private Date statisticsDate;
	private String	partner;
	private Integer	orderType;
	private Integer	selfScenes;
	private Integer	replacePeopleScenes;
	private Integer	receiveOrderCount;
	private Integer	arriveStartAreaOrderCount;
	private Integer	finishOrderCount;
	private Integer	carOwnerOrderCount;
	private Integer	cancelOrderCount;
	private Integer	receiveAfterOrderCancelCount;
	private Integer	arriveStartAraeAfterOrderCancelCount;
	private Integer	customerPrice;
	private Date	createTime;
}
