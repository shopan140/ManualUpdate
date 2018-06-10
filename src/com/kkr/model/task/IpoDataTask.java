package com.kkr.model.task;

import java.util.ArrayList;
import java.util.Map;

import com.kkr.model.IpoData;
import com.kkr.util.DateUtils;



public class IpoDataTask {
	
	public static ArrayList<IpoData> makeList(ArrayList<ArrayList<String>> list, Map<String, Integer> colMap, 
			String beginWeek, int header) {
		ArrayList<IpoData> eList = new ArrayList<IpoData>();
		int start = 0;
		if (header == 1) start = 1; 
		
		for(int i =start; i<list.size(); i++) {
			IpoData ed = new IpoData(list.get(i),colMap);
			if(ed.getOfferPrice()!=ed.getBadObjectMark() && ed.getPricingDate()!=null
					&& DateUtils.checkDate(beginWeek, ed.getPricingDate())) {
					eList.add(ed);
			}
		}
		return eList;
	}
}
