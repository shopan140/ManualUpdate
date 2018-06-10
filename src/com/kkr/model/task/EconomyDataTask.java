package com.kkr.model.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.kkr.model.EconomyData;
import com.kkr.util.CommonUtils;

public class EconomyDataTask {
	
	
	
	public static ArrayList<EconomyData> makeList(ArrayList<ArrayList<String>> list, Map<String, Integer> colMap, 
			int header) {
		ArrayList<EconomyData> eList = new ArrayList<EconomyData>();
		int start = 0;
		if (header == 1) start = 1; 
		
		for(int i =start; i<list.size(); i++) {
			EconomyData ed = new EconomyData(list.get(i),colMap);
			if(ed.getRelevance()!=ed.getBadObjectMark() && ed.getdDate()!=null 
					&& ed.getEvent().contains(ed.getExcludeContent())==false
					&&ed.getdTime().contains("/")==false) {
					eList.add(ed);
			}
		}
		return eList;
	}
	
	public static ArrayList<Double> makeListOfDouble(ArrayList<EconomyData> edL) {
		ArrayList<Double> oneDimList = new ArrayList<Double>();
		
		for (int i = 0; i < edL.size(); i++) {
			oneDimList.add(edL.get(i).getRelevance());
		}
		Collections.sort(oneDimList);
		return oneDimList;
	}
	
	public static void initializeImportance(ArrayList<EconomyData> edL) {
		for(int i =0; i<edL.size(); i++) {
			edL.get(i).setImportance(CommonUtils.percentile(makeListOfDouble(edL), edL.get(i).getRelevance()));
		}
	}

}
