package com.kkr.model;

public class CorrelationResult {
	
	private static final String defaultStatus = "Y";
	
	private String companyTicker1;
	private String companyTicker2;
	private double correlation;
	private String updateStatus = defaultStatus;
	
	public CorrelationResult() {
		super();
	}

	public CorrelationResult(String companyTicker1, String companyTicker2, double correlation) {
		super();
		this.companyTicker1 = companyTicker1;
		this.companyTicker2 = companyTicker2;
		this.correlation = correlation;
	}

	public String getCompanyTicker1() {
		return companyTicker1;
	}

	public void setCompanyTicker1(String companyTicker1) {
		this.companyTicker1 = companyTicker1;
	}

	public String getCompanyTicker2() {
		return companyTicker2;
	}

	public void setCompanyTicker2(String companyTicker2) {
		this.companyTicker2 = companyTicker2;
	}

	public double getCorrelation() {
		return correlation;
	}

	public void setCorrelation(double correlation) {
		this.correlation = correlation;
	}
	
	public String getUpdateStatus() {
		return updateStatus;
	}
	
	public void setUpdateStatus(String updateStatus) {
		this.updateStatus = updateStatus;
	}

	@Override
	public String toString() {
		return "CorrelationResult [companyTicker1=" + companyTicker1 + ", companyTicker2=" + companyTicker2
				+ ", correlation=" + correlation + ", updateStatus="+updateStatus+"]";
	}
	
	
	
	
	
	
}
