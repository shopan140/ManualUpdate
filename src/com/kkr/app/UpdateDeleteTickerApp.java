package com.kkr.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.kkr.model.task.UpdateDeleteTickerTask;
import com.kkr.util.DataBaseUtils;

public class UpdateDeleteTickerApp {
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		Connection cLocal = null;
		Connection cKkr=null;
		Connection ckkrProd=null;
		Connection cKkrClient=null;
		Connection ckkrProdClient=null;
		PreparedStatement pSLocal = null;
		try {
			cLocal = DataBaseUtils.connectLocal();
			cKkr=DataBaseUtils.connectkkrDev();
			ckkrProd=DataBaseUtils.connectkkrProd();
			cKkrClient=DataBaseUtils.connectkkrDevClient();
			ckkrProdClient=DataBaseUtils.connectkkrProdClient();
			
			//pSLocal = cLocal.prepareStatement(iStatement);
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		// TODO Auto-generated method stub
		UpdateDeleteTickerTask udtl=new UpdateDeleteTickerTask();
		udtl.createAllListnMaps(cKkr);
		udtl.CreateAddDeleteList(cKkr);
		
		Scanner input=new Scanner(System.in);
		System.out.println("checking price data for the ticker with start date:");
		String startDate=input.nextLine();
		System.out.println("end date for checking: ");
		String endDate=input.nextLine();
		udtl.finalDeletListBuild(startDate, endDate,cKkr);
		//udtl.DeleteFromTable(cKkr,cKkrClient);
	}
}
