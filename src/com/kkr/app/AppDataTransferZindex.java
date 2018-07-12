package com.kkr.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.kkr.util.DataBaseUtils;


public class AppDataTransferZindex {
	
	private static Connection conDev = null;
	private static Connection conProd = null;
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
		conDev = DataBaseUtils.connectkkrDevClient();
		conProd = DataBaseUtils.connectkkrProdClient();
		
		String sql = "Select timeseries_date, returns, close from user_saved_portfolio_timeseries where user_saved_portfolio_id = 130;";
		
		PreparedStatement pst = conDev.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		String insertSql = "Insert into user_saved_portfolio_timeseries (user_saved_portfolio_id,timeseries_date,returns,close) values(?,?,?,?);";
		PreparedStatement pstI = conProd.prepareStatement(insertSql);
		int i =1;
		while(rs.next()) {
			pstI.setInt(1, 131);
			pstI.setString(2, rs.getString("timeseries_date"));
			pstI.setString(3, rs.getString("returns"));
			pstI.setString(4, rs.getString("close"));
			
			pstI.executeUpdate();
			
			System.out.println(i);
			i++;
		}
		
		
		
	}

}
