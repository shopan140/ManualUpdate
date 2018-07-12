package com.kkr.app;

import java.sql.Connection;
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
	
	public static void main(String[] args) throws Exception {
		
		input = new Scanner(System.in);
		
		while(true) {
			System.out.println("Input Command {CORRProd,CorrDev, BKPDEV,BKPPROD, Break}: ");
			
			String command = input.nextLine();
			
			if(command.equalsIgnoreCase("CORRProd")) {
				
				
				conProd = DataBaseUtils.connectkkrProd();

				System.out.print("Input Start Date for price change (YYYY-MM-DD): ");
				String startDate = input.nextLine();
				System.out.print("Input end Date for price change (YYYY-MM-DD): ");
				String endDate = input.nextLine();
				
				correlationUpdate(conProd,startDate,endDate);
				correlationDelete(conProd,"N");
				
				conProd.close();
			}
			
			else if(command.equalsIgnoreCase("CORRDev")) {
				
				conDev = DataBaseUtils.connectkkrDev();

				System.out.print("Input Start Date for price change (YYYY-MM-DD): ");
				String startDate = input.nextLine();
				
				System.out.print("Input end Date for price change (YYYY-MM-DD): ");
				String endDate = input.nextLine();
				
				correlationUpdate(conDev,startDate,endDate);
				correlationDelete(conDev,"N");
				
				conDev.close();	
			}
			
			else if(command.equalsIgnoreCase("BKPDEV")) {
				conDev = DataBaseUtils.connectkkrDev();
				boolean backupCreated = DataBaseUtils.createBackup("zsenia_correlation_results", conDev, "");
				if(!backupCreated) {
					System.out.println("Taking backup of zsenia_correlation_results is unsuccessful.");
				}
				
				else {
					System.out.println("Taking backup of zsenia_correlation_results is successful.");
				}
				conDev.close();
			}
			
			else if(command.equalsIgnoreCase("BKPPROD")) {
				conProd = DataBaseUtils.connectkkrProd();
				boolean backupCreated = DataBaseUtils.createBackup("zsenia_correlation_results", conProd, "");
				if(!backupCreated) {
					System.out.println("Taking backup of zsenia_correlation_results is unsuccessful.");
				}
				
				else {
					System.out.println("Taking backup of zsenia_correlation_results is successful.");
				}
				
				conProd.close();
			}
			/*
			else if(command.equalsIgnoreCase("local")) {
				conLoc = DataBaseUtils.connectkkrProd();
				boolean backupCreated = DataBaseUtils.createBackup("zsenia_correlation_results", conLoc, "");
				if(!backupCreated) {
					System.out.println("Taking backup of zsenia_correlation_results is unsuccessful.");
				}
				else {
					System.out.println("Taking backup of zsenia_correlation_results is successful.");
				}
				conLoc.close();
			}
			*/
			else if(command.equalsIgnoreCase("Break")) {
				break;
			}
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
		int countN = CorrelationTask.countEntry(con, updateStatus);
		System.out.println("Number of + "+updateStatus+" in db: "+countN);
		
		if(countN<100000 && countN >0) {
			CorrelationTask.deleteOutdatedEntry(con, "N");
		}
	}
}
