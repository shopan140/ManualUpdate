package com.kkr.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {
	
	public static  Map<String, Integer> mapColumnIndex( ArrayList<String> head, String[] headings) {

		Map<String, Integer> colIndex = new HashMap<String, Integer>();

		if(head.size()>0) {
			for(String x:headings) {
				for(int i =0; i<head.size(); i++) {
					if(x.equalsIgnoreCase(head.get(i))) colIndex.put(x, i);
				}
			}
		}
		return colIndex;
	}

	public static void insertHeading(ArrayList<String> head, String heading, int postion) {
		if(head.size()>=postion) head.set(postion, heading);
	}
	
	
	public static int percentile(ArrayList<Double> list, double num) {
		int ranking =1;
		int j = 1;
		if(!list.isEmpty()) {
			Collections.sort(list);
			for(int i =0; i <list.size()-1; i++) {
				if(num>list.get(i) && num<=list.get(i+1)) {
					j = i+1;
					break;
				}
				
			}
			ranking = (int) Math.round(j*1.0/list.size()*10);
		}
		
		return ranking;
	}
	
	public static double getMax(ArrayList<Double> list) {
		double num = 0.0;
		
		if(list.size() > 1) {
			for(int i = 0; i < list.size()-1; i++) {
				if(list.get(i)<list.get(i+1)) {
					num = list.get(i+1);
				}
				else {
					num = list.get(i);
				}
			}
		}
		else {
			num = list.get(0);
		}
		
		return num;
	}
	
	public static double getMin(ArrayList<Double> list) {
		double num = 0.0;
		
		if(list.size() > 1) {
			for(int i = 0; i < list.size()-1; i++) {
				if(list.get(i)>list.get(i+1)) {
					num = list.get(i+1);
				}
				else {
					num = list.get(i);
				}
			}
		}
		else {
			num = list.get(0);
		}
		
		return num;
	}
	
	public static ArrayList<Double> makeList(ArrayList<ArrayList<String>> list, int columnIndex) {
		ArrayList<Double> oneDimList = new ArrayList<Double>();
		
		for(int i =1; i<list.size();i++) {
			String value = list.get(i).get(columnIndex);
			oneDimList.add(Double.parseDouble(value));
		}
		Collections.sort(oneDimList);
		return oneDimList;
		
	}
	
	public static void print2D(ArrayList<ArrayList<String>> list) {
		for(int i =0; i <list.size(); i++) {
			for(int j=0;j<list.get(i).size();j++) {
				System.out.print(list.get(i).get(j)+"***");
			}
			System.out.println("");
		}
	}
	
	public static<T> void print(ArrayList<T> list) {
		for(int i =0; i <list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
	}

}
