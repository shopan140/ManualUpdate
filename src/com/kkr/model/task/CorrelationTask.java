package com.kkr.model.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CorrelationTask {
	
	public static double MINIMUM_MCAP;
	public static String TIME_INTERVAL;
	public static double MINIMUM_VOL;
	public static int MINIMUM_PRICE_POINT;
	public static double MINIMUM_NET_ASSETS;
	
	
	public static ArrayList<Integer> companyIdList(Connection con) {
		ArrayList<Integer> tickerList = new ArrayList<Integer> ();
		
		String sql = "select kc.kkr_company_id from kkr_company as kc LEFT JOIN "
				+ "zsenia_equity_financials as zef on zef.kkr_company_id=kc.kkr_company_id "
				+ " LEFT JOIN kkr_price as kp on kp.KKR_company_ID=kc.kkr_company_id WHERE "
				+ " kc.Type='Equity' and zef.marketCap > 25000000 and kp.PRICE_DATE >(NOW() "
				+ "-INTERVAL 3 MONTH) and zef.endDate > (NOW() -INTERVAL 3 MONTH)"
				+ " GROUP BY kp.KKR_company_ID HAVING avg(kp.volume) > 25000;";
		
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				tickerList.add(rs.getInt(1));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return tickerList;
	}
	
	public static ArrayList<Integer> fundIdList(Connection con) {
		ArrayList<Integer> tickerList = new ArrayList<Integer> ();
		
		String sql = "select kc.kkr_company_id from kkr_company as kc "
				+ "LEFT JOIN kkr_fund_view as zef on zef.kkr_company_id=kc.kkr_company_id  "
				+ "LEFT JOIN kkr_price as kp on kp.KKR_company_ID=kc.kkr_company_id WHERE  "
				+ "kc.Type in ('etf', 'money market fund', 'Closed-End Fund') "
				+ "and zef.netassets > 25000000 and kp.PRICE_DATE >(NOW() -INTERVAL 3 MONTH) "
				+ "GROUP BY kp.KKR_company_ID HAVING avg(kp.volume) > 25000;";
		
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				tickerList.add(rs.getInt(1));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return tickerList;
	}
	
	public static Map<String, Double> mapDailyReturnAndDate(Connection con, 
			int companyId, String startDate, String endDate) {
		Map<String, Double> returnNdate = new HashMap<String, Double>();
		
		String sql = "Select PRICE_DATE, change_percent from kkr_price where kkr_company_id ="
				+companyId+" and PRICE_DATE >"+startDate +" and PRICE_DATE <= "+endDate;
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				returnNdate.put(rs.getString(1), rs.getDouble(2));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return returnNdate;
	}
	
	public static double correlation(Map<String, Double> id1, Map<String, Double> id2) {
		double corr=0;
		
		ArrayList<Double> data1 = new ArrayList<Double>();
		ArrayList<Double> data2 =new ArrayList<Double>();
		
		for(Map.Entry<String, Double> entry:id1.entrySet()) {
			if(id2.containsKey(entry.getKey())) {
				data1.add(entry.getValue());
				data2.add(id2.get(entry.getKey()));
			}
		}
		if(data1.size()==data2.size()&&data1.isEmpty()==false) {
			corr = getCorrelation(data1,data2);
		}
		
		return corr;
	}
	
	public static double getCorrelation(List<Double> data1, List<Double> data2) {
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = data1.get(0);
		double mean_y = data2.get(0);
		for (int i = 2; i < data1.size() + 1 && i <= data2.size(); i += 1) {
			double sweep = Double.valueOf(i - 1) / i;
			double delta_x = data1.get(i - 1) - mean_x;
			double delta_y = data2.get(i - 1) - mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x / data1.size());
		double pop_sd_y = (double) Math.sqrt(sum_sq_y / data1.size());
		double cov_x_y = sum_coproduct / data1.size();
		if(pop_sd_x != 0 && pop_sd_y != 0) {
			return cov_x_y / (pop_sd_x * pop_sd_y);
		} else {
			return 0;
		}
	}

}
