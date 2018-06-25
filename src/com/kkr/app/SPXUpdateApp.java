package com.kkr.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import com.kkr.model.SPXTicker;
import com.kkr.model.task.SPXTickerTask;
import com.kkr.util.CommonUtils;
import com.kkr.util.DataBaseUtils;
import com.kkr.util.ReadFile;

public class SPXUpdateApp {
	
	
	private static final int header = 1;
	private static Scanner input = null;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
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
					ArrayList<SPXTicker> eList = new ArrayList<SPXTicker>();
					Map<String,Integer> colMap = CommonUtils.mapColumnIndex(dataHolder.get(0), SPXTickerTask.headings);
					System.out.println(colMap);
					eList = SPXTickerTask.makeList(dataHolder, colMap, header);
					System.out.println(eList.size());
					Connection conDev = DataBaseUtils.connectkkrDev();
					Connection conProd = DataBaseUtils.connectkkrProd();
//					Connection conLoc = DataBaseUtils.connectLocal();
//					SPXTickerTask.updateSP500(conLoc, eList);
					SPXTickerTask.updateSP500(conDev, eList);
					SPXTickerTask.updateSP500(conProd, eList);
					
					conDev.close();
					conProd.close();
//					conLoc.close();
					
				}
			}
			else if(command.equalsIgnoreCase("break")) break;
			
			
		}
	}

}
