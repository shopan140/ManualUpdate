package com.kkr.model;

import java.util.ArrayList;
import java.util.Map;

import com.kkr.util.DateUtils;

public class IpoData {
	private static final double badObject = -1;
	
	private String pricingDate;
	private String issueName;
	private String primaryExchange;
	private double offerSize;
	private double offerPrice;
	private double badObjectMark = badObject;
	
	public static String[] heading = {"Pricing Date","Issuer Name","Primary Exchange",
			"Offer Size (M)","Offer Price"};
	
	
	public String getPricingDate() {
		return pricingDate;
	}
	public void setPricingDate(String pricingDate) {
		this.pricingDate = pricingDate;
	}
	public String getIssueName() {
		return issueName;
	}
	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}
	public String getPrimaryExchange() {
		return primaryExchange;
	}
	public void setPrimaryExchange(String primaryExchange) {
		this.primaryExchange = primaryExchange;
	}
	public double getOfferSize() {
		return offerSize;
	}
	public void setOfferSize(double offerSize) {
		this.offerSize = offerSize;
	}
	public double getOfferPrice() {
		return offerPrice;
	}
	public void setOfferPrice(double offerPrice) {
		this.offerPrice = offerPrice;
	}
	
	public double getBadObjectMark() {
		return badObjectMark;
	}
	public void setBadObjectMark(double badObjectMark) {
		this.badObjectMark = badObjectMark;
	}
	@Override
	public String toString(){
		return "Pricing Date: "+ pricingDate+" Issue Name: "+ issueName+" Primary Exchance: "+
				primaryExchange+" Offer Size: "+ offerSize+" Offer Price: "+offerPrice;
	}
	
	public IpoData(ArrayList<String> list, Map<String, Integer> colMap) {

		try {
			if (list.size()>4) {
				if(list.get(colMap.get(heading[0])).equals("")==false) {
					this.pricingDate = DateUtils.convertDate(list.get(colMap.get(heading[0])));
				}
				this.issueName = list.get(colMap.get(heading[1]));
				this.primaryExchange = list.get(colMap.get(heading[2]));
				this.offerSize = Double.parseDouble(list.get(colMap.get(heading[3])));
				this.offerPrice =Double.parseDouble(list.get(colMap.get(heading[4])));
			}

		} catch(Exception e) {
			this.offerPrice = badObject;
			this.offerSize=badObject;
		}

	}
}