package com.kkr.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.kkr.model.task.HistoricalPriceEventGenrationTask;
import com.kkr.util.DataBaseUtils;

public class HistoricalPriceEventGenrationApp {

	public static void main(String []args) throws Exception{
		System.out.println("enter a date where to start calculating the SnP:");
		Scanner input = new Scanner(System.in);
		String d_date = input.nextLine();

		Connection con = DataBaseUtils.connectLocal();
		Connection conKKrProd = DataBaseUtils.connectkkrProd();
		Connection conKkrDev = DataBaseUtils.connectkkrDev();
		
		HistoricalPriceEventGenrationTask.HistoricalPriceEventLoader(con,conKKrProd,conKkrDev,d_date);
	}

}
