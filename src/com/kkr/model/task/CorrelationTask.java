package com.kkr.model.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kkr.model.CorrelationResult;


public class CorrelationTask {
	
	public static CorrelationResult getCorrelationResult(Connection con,Map<String, Double> map1, Map<String,
			Double> map2, Integer id1, Integer id2) {
		Double corr = getCorrelation(map1,map2);
		String ticker1 = getCompanyTicker(con,id1);
		String ticker2 = getCompanyTicker(con,id2);
		CorrelationResult cr = new CorrelationResult(ticker1,ticker2,corr);
		return cr;
	}
	
	public static ArrayList<Map<String, Double>> getMapList(Connection con, ArrayList<Integer> combineList,
			String startDate, String endDate) {
		
		ArrayList<Map<String, Double>> mapList = new ArrayList<Map<String, Double>>();
		for(int i = 0; i <combineList.size(); i++) {
			mapList.add(mapDailyReturnAndDate(con,combineList.get(i),startDate,endDate));
		}
		return mapList;
	}
	
	
	public static Map<String, Double> mapDailyReturnAndDate(Connection con, 
			Integer companyId, String startDate, String endDate) {
		Map<String, Double> returnNdate = new HashMap<String, Double>();
		
		String sql = "select price_date, change_percent from kkr_price where kkr_company_id = "
				+ companyId+" and Price_date> '"+startDate+"' and price_date<='"+endDate+"';";
		
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				returnNdate.put(rs.getString("PRICE_DATE"), rs.getDouble("change_percent"));
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return returnNdate;
	}
	
	
	private static double getCorrelation(Map<String, Double> tickerMap1, Map<String, Double> tickerMap2) {
		double corr=0;
		
		ArrayList<Double> data1 = new ArrayList<Double>();
		ArrayList<Double> data2 =new ArrayList<Double>();
		System.out.println("Map1  "+tickerMap1.size());
		System.out.println("Map2 "+tickerMap2.size());
		for(Map.Entry<String, Double> entry:tickerMap1.entrySet()) {
			if(tickerMap2.containsKey(entry.getKey())) {
				data1.add(entry.getValue());
				data2.add(tickerMap2.get(entry.getKey()));
			}
		}
		
		if(data1.size()==data2.size()&&data1.isEmpty()==false) {
			corr = getCorrelation(data1,data2);
		}
		
		System.out.println("List1 "+data1.size());
		System.out.println("List2 "+data2.size());
		return corr;
	}
	
	public static ArrayList<Integer> companyIdList(Connection con, double minimumMcap,
			String timeInterval, double minimumVol) {
		ArrayList<Integer> tickerList = new ArrayList<Integer> ();
		
		String sql = "select kc.kkr_company_id from kkr_company as kc LEFT JOIN "
				+ "zsenia_equity_financials as zef on zef.kkr_company_id=kc.kkr_company_id "
				+ " LEFT JOIN kkr_price as kp on kp.kkr_company_id=kc.kkr_company_id WHERE "
				+ " kc.Type='Equity' and zef.marketCap >"+ minimumMcap+" and kp.PRICE_DATE >(NOW() - "
				+ timeInterval+") and zef.endDate > (NOW() - "+ timeInterval
				+ ") GROUP BY kp.kkr_company_id HAVING avg(kp.volume) >"+ minimumVol+";";
		
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
	
	public static ArrayList<Integer> fundIdList(Connection con, double minimumAsset,
			String timeInterval, double minimumVol) {
		ArrayList<Integer> tickerList = new ArrayList<Integer> ();
		
		String sql = "select kc.kkr_company_id from kkr_company as kc "
				+ "LEFT JOIN kkr_fund_view as zef on zef.kkr_company_id=kc.kkr_company_id  "
				+ "LEFT JOIN kkr_price as kp on kp.kkr_company_id=kc.kkr_company_id WHERE  "
				+ "kc.Type in ('etf', 'money market fund', 'Closed-End Fund') "
				+ "and zef.netassets > "+ minimumAsset+" and kp.PRICE_DATE >(NOW() -"+ timeInterval+") "
				+ "GROUP BY kp.kkr_company_id HAVING avg(kp.volume) > "+ minimumVol+";";
		
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
	
	
	public static String getCompanyTicker(Connection con, Integer companyId) {
		String companyTicker = null;
		String sqlTicker = "select company_ticker from kkr_company where kkr_company_id = "+companyId;
		try {
			PreparedStatement pstm = con.prepareStatement(sqlTicker);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				companyTicker = rs.getString(1);
			}
		} catch(Exception e) {
			System.out.println("Ticker not found for this "+companyId);
		}
		return companyTicker;
	}	
	
	
	
	
	
	
	
	
	
	
	private static double getCorrelation(ArrayList<Double> data1, ArrayList<Double> data2) {
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
