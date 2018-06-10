package com.kkr.model;

public class SPXTicker {
	
	private static final double badObject = -1;
	public static final String[] heading = {"Ticker"};
	private String tickerName;
	private double badObjectMark = badObject;
	
	
	public void setTickerName(String tickerName) {
		if(tickerName.contains(" ")==false) this.tickerName = tickerName;
		
	}
	
	public String getTickerName() {
		return tickerName;
	}
	
	public void setBadObjectMark(double badObjectMark) {
		this.badObjectMark = badObjectMark;
	}
	
	public double getBadObjectMark() {
		return badObjectMark;
	}
	
	public String toString() {
		return "Ticker Name: "+tickerName+", Object Status: "+badObjectMark;
	}
}
