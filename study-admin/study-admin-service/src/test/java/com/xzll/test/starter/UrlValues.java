package com.xzll.test.starter;

import java.util.List;

/**
 * @Author: hzz
 * @Date: 2022/9/23 15:37:52
 * @Description:
 */
public class UrlValues {
	private String areaCode;
	private List<Integer> orderType;
	private String startTime;
	private String endTime;
	private Integer timeType;
	private Double lat;
	private Double lng;

	private String pushloctime;
	private String pushordertips;
	private String pushtimetitle;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public List<Integer> getOrderType() {
		return orderType;
	}

	public void setOrderType(List<Integer> orderType) {
		this.orderType = orderType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getTimeType() {
		return timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getPushloctime() {
		return pushloctime;
	}

	public void setPushloctime(String pushloctime) {
		this.pushloctime = pushloctime;
	}

	public String getPushordertips() {
		return pushordertips;
	}

	public void setPushordertips(String pushordertips) {
		this.pushordertips = pushordertips;
	}

	public String getPushtimetitle() {
		return pushtimetitle;
	}

	public void setPushtimetitle(String pushtimetitle) {
		this.pushtimetitle = pushtimetitle;
	}
}
