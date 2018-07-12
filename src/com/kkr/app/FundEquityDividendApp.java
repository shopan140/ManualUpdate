package com.kkr.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.kkr.model.task.FundEquityDividendTask;
import com.kkr.util.DataBaseUtils;

public class FundEquityDividendApp {
	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
		Scanner input = new Scanner(System.in);
		System.out.println("Quote Media Query paramters start date:(yyyy-MM-dd) ");
		String start_date = input.nextLine();
		System.out.println("Quote Media Query paramters end date:(yyyy-MM-dd) ");
		String end_date = input.nextLine();

		Connection ConKkrProd = DataBaseUtils.connectkkrProd();
		Connection ConKkrDev = DataBaseUtils.connectkkrDev();
		FundEquityDividendTask.FundEquityDividendProcess(ConKkrProd,ConKkrDev,start_date,end_date);
	}

}
