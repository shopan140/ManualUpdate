package com.kkr.app;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.kkr.model.EconomyData;
import com.kkr.model.IpoData;
import com.kkr.model.task.EconomyDataTask;
import com.kkr.model.task.IpoDataTask;
import com.kkr.util.CommonUtils;
import com.kkr.util.DataBaseUtils;
import com.kkr.util.ReadFile;

public class EcoIpoApp {
	
//	public static final String format = "mm/dd/yy";
	private static String beginDate;
	private static final int positionPrior = 5;
	private static final int header = 1;
	private static Scanner input = null;
	
	
	public static void main(String [] agrs) throws ClassNotFoundException, SQLException {
		
		Connection conP = DataBaseUtils.connectkkrProd();
		Connection conD = DataBaseUtils.connectkkrDev();
//		Connection conL = DataBaseUtils.connectLocal();
		String inputFile = "/Users/Shopan/Desktop/raw.csv";
		
		input = new Scanner(System.in);
		
		while(true) {
			System.out.print("Input Command for task {ECON, IPO, Break}: ");
			String command = input.nextLine();
			if(command.equalsIgnoreCase("Break")) {
				System.out.println("Program Closed");
				break;
			} 
			
			else if(command.equalsIgnoreCase("ECON")) {
				ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
				
				System.out.println("Provide the full path of input file: ");
				inputFile = input.nextLine();
				list = ReadFile.read(inputFile);
				if (!list.isEmpty()) {
					CommonUtils.insertHeading(list.get(0), "Period", positionPrior);
					Map<String,Integer> colIndex = new HashMap<String, Integer>();
					colIndex = CommonUtils.mapColumnIndex(list.get(0),EconomyData.heading);
					ArrayList<EconomyData> edList = new ArrayList<EconomyData> ();
					edList = EconomyDataTask.makeList(list, colIndex,header);
					EconomyDataTask.initializeImportance(edList);
					insertEconomyData(conP,edList);
					insertEconomyData(conD,edList);
					
				}
				else System.out.println("Start over aggin");
			}
			else if(command.equalsIgnoreCase("IPO")) {
				System.out.println("Provide the full path of input file: ");
				inputFile = input.nextLine();
				System.out.print("Input the Beginning Date(YYYY-MM-DD) of the week: ");
				beginDate = input.nextLine();
				
				ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
				list = ReadFile.read(inputFile);
				if (!list.isEmpty()) {
					Map<String,Integer> colIndex = new HashMap<String, Integer>();
					colIndex = CommonUtils.mapColumnIndex(list.get(0),IpoData.heading);
					ArrayList<IpoData> edList = new ArrayList<IpoData> ();
					edList = IpoDataTask.makeList(list, colIndex,beginDate,header); 
					insertIpoData(conP,edList);
					insertIpoData(conD,edList);
				}
					
				else System.out.println("Start over aggin");
				
			}
			else if(command.equalsIgnoreCase("break")) 
				conP.close();
				conD.close();
//				conL.close();
				return;
		}
		
	}
	
	
	private static void insertEconomyData(Connection con, ArrayList<EconomyData> edList) {
		String sql = "insert into kkr_economy_data (economy_date,time,event,period,survey,"
				+ "actual,prior, revised,importance) values (?,?,?,?,?,?,?,?,?)";
		try {

			PreparedStatement pt = con.prepareStatement(sql);
			for(int i = 0; i < edList.size(); i++) {
				pt.setString(1, edList.get(i).getdDate());
				pt.setString(2, edList.get(i).getdTime());
				pt.setString(3, edList.get(i).getEvent());
				pt.setString(4, edList.get(i).getPeriod());
				pt.setString(5, edList.get(i).getSurvey());
				pt.setString(6, edList.get(i).getActual());
				pt.setString(7, edList.get(i).getPrior());
				pt.setString(8, edList.get(i).getRevised());
				pt.setInt(9, edList.get(i).getImportance());
				
				pt.addBatch();
			}
			
			String sqlt = "TRUNCATE TABLE kkr_economy_data";
			PreparedStatement pt1 = con.prepareStatement(sqlt);
			pt1.executeUpdate();
			System.out.println("Economy Table Truncated");
			pt.executeBatch();
			System.out.println("Economy Table Updated");
		} catch(Exception e) {
			System.out.println("Insertion error in Economy");
		}
	}
	
	private static void insertIpoData(Connection con, ArrayList<IpoData> edList) {
		String sql = "insert into kkr_ipo_data (effective_date,issuer_name,primary_exchange,"
				+ "offer_size,offer_price) values (?,?,?,?,?)";
		try {
			PreparedStatement pt = con.prepareStatement(sql);
			for(int i = 0; i < edList.size(); i++) {
				pt.setString(1, edList.get(i).getPricingDate());
				pt.setString(2, edList.get(i).getIssueName());
				pt.setString(3, edList.get(i).getPrimaryExchange());
				pt.setDouble(4, edList.get(i).getOfferSize());
				pt.setDouble(5, edList.get(i).getOfferPrice());
				pt.addBatch();
			}
			
			String sqlt = "TRUNCATE TABLE kkr_ipo_data";
			PreparedStatement pt1 = con.prepareStatement(sqlt);
			pt1.executeUpdate();
			System.out.println("Ipo Table Truncated");
			pt.executeBatch();
			System.out.println("Ipo Table Updated");
		} catch(Exception e) {
			System.out.println("Insertion error in IPO");
		}
		
	}
	

}
