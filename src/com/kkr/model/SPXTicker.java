package com.kkr.model;

public class SPXTicker {
	
	
	
	private String tickerName;
	private boolean badObject = false;
	private static final int position = 0;
	
	
	public void setTickerName(String tickerName) {
		if(tickerName.contains(" ")==true) {
			String[] tickers = tickerName.split(" ");
			if(tickers[position].equals("")==false) {
				this.tickerName = tickers[position];
			} else setBadObject(true);
			
		} else {
			if(tickerName.equals("")==false) {
				this.tickerName = tickerName;
			} else setBadObject(true);
		} 
		
		
	}
	
	public String getTickerName() {
		return tickerName;
	}
	
	public void setBadObject(boolean badObjectMark) {
		this.badObject = badObjectMark;
	}
	
	public boolean getBadObject() {
		return badObject;
	}
	
	public String toString() {
		return "Ticker Name: "+tickerName+", Object Status: "+badObject;
	}
}
