package com.kkr.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.kkr.model.CorrelationResult;
import com.kkr.model.task.CorrelationTask;
import com.kkr.util.DataBaseUtils;

public class CorrelationApp {
	
	public static final double MINIMUM_MCAP = 2500000;
	public static final String TIME_INTERVAL = " INTERVAL 3 MONTH";
	public static final double MINIMUM_VOL = 25000;
	public static final double MINIMUM_NET_ASSETS = 25000000;
	
	public static final String startDate ="2017-12-31";
	public static final String endDate ="2018-06-12";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Connection conDev = DataBaseUtils.connectkkrDev();
//		Connection conProd = DataBaseUtils.connectkkrProd();
//		Connection conLoc = DataBaseUtils.connectLocal();
		
		ArrayList<Integer> combineList = new ArrayList<Integer> ();
//		combineList.addAll(CorrelationTask.companyIdList(conDev, MINIMUM_MCAP, TIME_INTERVAL, MINIMUM_VOL));
//		combineList.addAll(CorrelationTask.companyIdList(conDev, MINIMUM_MCAP, TIME_INTERVAL, MINIMUM_NET_ASSETS));
		combineList.add(5);
		combineList.add(4);
		System.out.println("Total Number of Ticker: "+combineList.size());
		ArrayList<Map<String, Double>> mapList=CorrelationTask.getMapList(conDev, combineList, startDate, endDate);
		
//		print(combineList);
		for(int i = 0; i<combineList.size(); i++) {
			for(int j =i+1; j <combineList.size(); j++) {
				CorrelationResult cr = new CorrelationResult();
				cr = CorrelationTask.getCorrelationResult(conDev, mapList.get(i), mapList.get(j),
						combineList.get(i), combineList.get(j));
//				writeTodb(conDev,cr,i,j);
//				writeTodb(conProd,cr,i,j);
				System.out.println(cr.toString());
			}
		}
		
		System.out.println("Correlation data Insertion Finished.");
	}
	
	
	public static void writeTodb(Connection con, CorrelationResult cr, int i, int j) {
		String sql = "Insert into zsenia_correlation_results (ticker1, ticker2, correlation) values(?,?,?);";
		try {
			PreparedStatement pstm = con.prepareStatement(sql);
			pstm.setString(1, cr.getCompanyTicker1());
			pstm.setString(2, cr.getCompanyTicker2());
			pstm.setDouble(3, cr.getCorrelation());
			pstm.executeUpdate();
		} catch(Exception e) {
			System.out.println("Error while inseting data into correlation resutls table."
					+"while i ="+i+" and j= "+j);
		}
		
	}

}
