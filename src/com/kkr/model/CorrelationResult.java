package com.kkr.model;

public class CorrelationResult {
	
	private String ticker1;
	private String ticker2;
	private double correlation;
	
	public String getTicker1() {
		return ticker1;
	}
	public void setTicker1(String ticker1) {
		this.ticker1 = ticker1;
	}
	public String getTicker2() {
		return ticker2;
	}
	public void setTicker2(String ticker2) {
		this.ticker2 = ticker2;
	}
	public double getCorrelation() {
		return correlation;
	}
	public void setCorrelation(double correlation) {
		this.correlation = correlation;
	}
	@Override
	public String toString() {
		return "CorrelationResult [ticker1=" + ticker1 + ", ticker2=" + ticker2 + ", correlation=" + correlation + "]";
	}
	
	public CorrelationResult(String ticker1, String ticker2, double correlation) {
		super();
		this.ticker1 = ticker1;
		this.ticker2 = ticker2;
		this.correlation = correlation;
	}
	
}
