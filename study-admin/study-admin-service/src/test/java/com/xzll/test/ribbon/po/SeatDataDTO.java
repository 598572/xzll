package com.xzll.test.ribbon.po;


public class SeatDataDTO {

	private Integer currentConnectCount;
	private Integer todayConnectCount;
	private String seatNumber;

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Integer getCurrentConnectCount() {
		return currentConnectCount;
	}

	public void setCurrentConnectCount(Integer currentConnectCount) {
		this.currentConnectCount = currentConnectCount;
	}

	public Integer getTodayConnectCount() {
		return todayConnectCount;
	}

	public void setTodayConnectCount(Integer todayConnectCount) {
		this.todayConnectCount = todayConnectCount;
	}
}
