package com.kkr.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import com.kkr.model.SPXTicker;
import com.kkr.model.task.SPXTickerTask;
import com.kkr.util.CommonUtils;
import com.kkr.util.DataBaseUtils;
import com.kkr.util.DateUtils;
import com.kkr.util.ReadFile;

public class App {
	
	private static final int header = 1;
	private static Scanner input = null;
	private static String createBackupQuery = "create table zsenia_backup.Z_bkp_<DATE>_<TABLE_NAME>  as select * from <TABLE_NAME>";
	public static void main(String[] args) throws Exception {
		
		input = new Scanner(System.in);
		
		while(true) {
			System.out.println("Input command {SPX, break}: ");
			String command = input.nextLine();
			if(command.equalsIgnoreCase("SPX")) {
				System.out.println("Input the file path and name for SP500 ticker list: ");
				String fileName = input.nextLine();
				ArrayList<ArrayList<String>> dataHolder = new ArrayList<ArrayList<String>>();
				dataHolder = ReadFile.read(fileName);
				System.out.println(dataHolder.size());
				if(!dataHolder.isEmpty()) {
					ReadFile.printArraylist(dataHolder);
				}
			}
			else if (command.equalsIgnoreCase("backup")) {
				Connection con = DataBaseUtils.connectLocal();
				
				boolean backupcreated = createBackup("kkr_ipo_data",con,"");
				if(backupcreated == true) {
					System.out.println("Backup successfull");
				}
				else System.out.println("Backup failed");
			}
			else if(command.equalsIgnoreCase("break")) {
				System.out.println("Program is closed,");
				break;
			}
			
			
		}
	}
	
	public  static boolean createBackup(String tableName, Connection con, String specialConditions) throws Exception {

		Statement stmt = null;	
		String query = createBackupQuery.replace("<DATE>", DateUtils.formatDate(new Date(), "ddMMyy_HH_MM_SS"));
		query=query.replace("<TABLE_NAME>", tableName);
		if(specialConditions!=null && !specialConditions.isEmpty()) {
			query=query+" "+specialConditions;
		}
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		return true;
	}


}
