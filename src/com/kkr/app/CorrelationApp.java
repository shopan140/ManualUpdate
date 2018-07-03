package com.kkr.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import com.kkr.model.CorrelationResult;
import com.kkr.model.task.CorrelationTask;
import com.kkr.util.DataBaseUtils;


public class CorrelationApp {
	
	private static Connection conDev = null;
	private static Connection conProd = null;
	private static Connection conLoc = null;
	
	public static final double MINIMUM_MCAP = 2500000;
	public static final String TIME_INTERVAL = " INTERVAL 3 MONTH";
	public static final double MINIMUM_VOL = 25000;
	public static final double MINIMUM_NET_ASSETS = 25000000;
	
//	public static final String startDate ="2017-12-31";
//	public static final String endDate ="2018-06-12";
	
	private static Scanner input = null;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		input = new Scanner(System.in);
		
		while(true) {
			System.out.println("Input Command {CORRProd,CorrDev, Break}: ");
			
			String command = input.nextLine();
			
			if(command.equalsIgnoreCase("CORRProd")) {
				
				
				conProd = DataBaseUtils.connectkkrProd();
//				conLoc = DataBaseUtils.connectLocal();
				
				System.out.print("Input end Date for price change (YYYY-MM-DD): ");
				String endDate = input.nextLine();
				
				System.out.print("Input Start Date for price change (YYYY-MM-DD): ");
				String startDate = input.nextLine();
				
				correlationUpdate(conProd,startDate,endDate);
				correlationDelete(conProd,"N");
				

				conProd.close();
//				conLoc.close();
				
				
			}
			
			else if(command.equalsIgnoreCase("CORRDev")) {
				
				conDev = DataBaseUtils.connectkkrDev();

				
				System.out.print("Input end Date for price change (YYYY-MM-DD): ");
				String endDate = input.nextLine();
				
				System.out.print("Input Start Date for price change (YYYY-MM-DD): ");
				String startDate = input.nextLine();
				
				correlationUpdate(conDev,startDate,endDate);
				correlationDelete(conDev,"N");
				
				conDev.close();
				
				
			}
			else if(command.equalsIgnoreCase("Break")) {
				break;
			}
		}
		
	}
	
	
	
	
	private static void correlationUpdate(String startDate, String endDate) {
		
		ArrayList<Integer> combineList = new ArrayList<Integer> ();
		combineList.addAll(CorrelationTask.companyIdList(conProd, MINIMUM_MCAP, TIME_INTERVAL, MINIMUM_VOL));
		combineList.addAll(CorrelationTask.companyIdList(conProd, MINIMUM_MCAP, TIME_INTERVAL, MINIMUM_NET_ASSETS));
		
		System.out.println("Total Number of Ticker: "+combineList.size());
		ArrayList<Map<String, Double>> mapList=CorrelationTask.getMapList(conProd, combineList, startDate, endDate);
		CorrelationTask.changeUpdateStatus(conProd, "N");
		CorrelationTask.changeUpdateStatus(conProd, "N");
//		CorrelationTask.changeUpdateStatus(conLoc, "N");

		for(int i = 0; i<combineList.size(); i++) {
			for(int j =i+1; j <combineList.size(); j++) {
				CorrelationResult cr = new CorrelationResult();
				cr = CorrelationTask.getCorrelationResult(conProd, mapList.get(i), mapList.get(j),
						combineList.get(i), combineList.get(j));

				if(cr.getCompanyTicker1()!=null && cr.getCompanyTicker2()!=null) {
					CorrelationTask.writeTodb(conDev,cr,i,j);
					CorrelationTask.writeTodb(conProd,cr,i,j);
//					CorrelationTask.writeTodb(conLoc,cr,i,j);
				}
				else System.out.println("One of the ticker is null.");
			}
		}
	}
	
	private static void correlationDelete(String updateStatus) {
		int countNdev = CorrelationTask.countEntry(conDev, updateStatus);
		System.out.println("Number of + "+updateStatus+" in stage db: "+countNdev);
		int countNprod =CorrelationTask.countEntry(conProd, "N");
		System.out.println("Number of + "+updateStatus+" in prod db: "+countNprod);
		
		if(countNdev<1000000 && countNdev >0) {
			CorrelationTask.deleteOutdatedEntry(conDev, "N");
		}
		if(countNprod<1000000 && countNprod >0) {
			CorrelationTask.deleteOutdatedEntry(conProd, "N");
		}
	}
	
	
private static void correlationUpdate(Connection con, String startDate, String endDate) {
		
		ArrayList<Integer> combineList = new ArrayList<Integer> ();
		combineList.addAll(CorrelationTask.companyIdList(con, MINIMUM_MCAP, TIME_INTERVAL, MINIMUM_VOL));
		combineList.addAll(CorrelationTask.companyIdList(con, MINIMUM_MCAP, TIME_INTERVAL, MINIMUM_NET_ASSETS));
		
		System.out.println("Total Number of Ticker: "+combineList.size());
		ArrayList<Map<String, Double>> mapList=CorrelationTask.getMapList(con, combineList, startDate, endDate);
		CorrelationTask.changeUpdateStatus(con, "N");

		for(int i = 0; i<combineList.size(); i++) {
			for(int j =i+1; j <combineList.size(); j++) {
				CorrelationResult cr = new CorrelationResult();
				cr = CorrelationTask.getCorrelationResult(con, mapList.get(i), mapList.get(j),
						combineList.get(i), combineList.get(j));

				if(cr.getCompanyTicker1()!=null && cr.getCompanyTicker2()!=null) {
					CorrelationTask.writeTodb(con,cr,i,j);
				}
				else System.out.println("One of the ticker is null.");
			}
		}
	}
	
	private static void correlationDelete(Connection con, String updateStatus) {
		int countNdev = CorrelationTask.countEntry(con, updateStatus);
		System.out.println("Number of + "+updateStatus+" in stage db: "+countNdev);
		int countNprod =CorrelationTask.countEntry(conProd, "N");
		System.out.println("Number of + "+updateStatus+" in prod db: "+countNprod);
		
		if(countNdev<1000000 && countNdev >0) {
			CorrelationTask.deleteOutdatedEntry(con, "N");
		}
		if(countNprod<1000000 && countNprod >0) {
			CorrelationTask.deleteOutdatedEntry(con, "N");
		}
	}
}
