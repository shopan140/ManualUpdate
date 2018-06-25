package com.kkr.model.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;
import com.kkr.model.SPXTicker;


public class SPXTickerTask {
	public static final String[] headings = {"Ticker"};
	
	public static void updateSP500(Connection con, ArrayList<SPXTicker> tickerList) 
	{
		try
		{
			// update all the sp500 make to "N"
			String updateSQL = "update kkr_company set sp500 = 'N'";
			PreparedStatement setN = con.prepareStatement(updateSQL);
			setN.executeUpdate();
			
			// update New the sp500 make to "Y"
			for(int j =0; j <tickerList.size(); j++)
			{
				String sql = "UPDATE kkr_company SET sp500 = 'Y' WHERE Company_ticker = '"+tickerList.get(j).getTickerName()+"';";
				PreparedStatement updateY = con.prepareStatement(sql);
				updateY.executeUpdate();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<SPXTicker> makeList(ArrayList<ArrayList<String>> list, Map<String, Integer> colMap, 
			int header) {
		ArrayList<SPXTicker> eList = new ArrayList<SPXTicker>();
		int start = 0;
		if (header == 1) start = 1; 
		
		for(int i =start; i<list.size(); i++) {
			SPXTicker ed = new SPXTicker();
			
			ed.setTickerName(list.get(i).get(colMap.get("Ticker")));
			
			if(ed.getBadObject()!=true){
					eList.add(ed);
			}
			ed.setTickerName(list.get(i).get(0));
		}
		return eList;
	}

}
