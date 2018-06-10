package com.kkr.model;

import java.util.ArrayList;
import java.util.Map;
import com.kkr.util.DateUtils;

public class EconomyData {
	private static final double badObject = -1;
	private static final String bloomberg ="Bloomberg";
	
	private String dDate;
	private String dTime;
	private String event;
	private String period;
	private String survey;
	private String actual;
	private String prior;
	private String revised;
	private double relevance;
	private int importance;
	private double badObjectMark = badObject;
	private String excludeContent = bloomberg;
	
	
	public static String[] heading ={"Date Time", "Event","Period", 
			"Survey",	"Actual",	"Prior",	"Revised",	"Relevance"};
	
	public String getdDate() {
		return dDate;
	}
	public void setdDate(String dDate) {
		this.dDate = dDate;
	}
	public String getdTime() {
		return dTime;
	}
	public void setdTime(String dTime) {
		this.dTime = dTime;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getSurvey() {
		return survey;
	}
	public void setSurvey(String survey) {
		this.survey = survey;
	}
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public String getPrior() {
		return prior;
	}
	public void setPrior(String prior) {
		this.prior = prior;
	}
	public String getRevised() {
		return revised;
	}
	public void setRevised(String revised) {
		this.revised = revised;
	}
	public double getRelevance() {
		return relevance;
	}
	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	
	public double getBadObjectMark() {
		return badObjectMark;
	}
	public void setBadObjectMark(double badObjectMark) {
		this.badObjectMark = badObjectMark;
	}
	public String getExcludeContent() {
		return excludeContent;
	}
	public void setExcludeContent(String excludeContent) {
		this.excludeContent = excludeContent;
	}
	
	@Override
	public String toString(){
		return "Date: " + dDate +" Time: "+dTime+" Event: "+event +" Period: "+ period+
				" Survey: "+survey+" Actual: "+actual+" Prior: "+ prior+" Revised: "+revised+
				" Relevance: "+relevance+" Importance: "+ importance +" Bad Object Mark "
				+ badObject +" Exclude Content: "+excludeContent;
	}
	
	public EconomyData(ArrayList<String> list, Map<String, Integer> colMap) {
		
		try {
			if (list.size()>7) {
				String[] dateTime = list.get(colMap.get(heading[0])).split(" ");
				this.dDate = DateUtils.convertDate(dateTime[0]);
				this.dTime = dateTime[1];
				this.event = list.get(colMap.get(heading[1]));
				this.period = list.get(colMap.get(heading[2]));
				this.survey = list.get(colMap.get(heading[3]));
				this.actual = list.get(colMap.get(heading[4]));
				this.prior = list.get(colMap.get(heading[5]));
				this.revised = list.get(colMap.get(heading[6]));
				this.relevance = Double.parseDouble(list.get(colMap.get(heading[7])));
		}
			
		} catch(Exception e) {
			this.relevance = badObject;
		}
		
	}
	

}
