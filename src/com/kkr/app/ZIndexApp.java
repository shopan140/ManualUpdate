package com.kkr.app;

import java.sql.Connection;
import java.util.Scanner;

import com.kkr.model.task.TreasureyYieldTask;
import com.kkr.model.task.VolatilityIndexDataUpdateTask;
import com.kkr.model.task.VolatilityIndexTask;
import com.kkr.model.task.ZIndexTask;
import com.kkr.util.DataBaseUtils;

public class ZIndexApp {
	
	public static void main(String [] args) throws Exception{
		
		Connection conL = DataBaseUtils.connectLocal();
		Connection conKkr = DataBaseUtils.connectkkrDevClient();
		Connection conkkrProd = DataBaseUtils.connectkkrProdClient();
		Scanner input=new Scanner(System.in);
		System.out.println("::Insert Treasury Yield Data to Local database by Scraping::");
		System.out.println("\n\n");
		System.out.println("Enter year if we want to fetch historical data");
		String year=input.nextLine();
		TreasureyYieldTask.treasuryWebsiteRead(year,conL);
		
		
		System.out.println("\n\n");
		System.out.println("Scrap Yahoo to inser Volatility Index Dta in local database");
		System.out.println("\n");
		System.out.println("insert date range for historical price data fetch.");
		System.out.println("if not input provided then take automatically 1 year date range from current date\n");
		
		System.out.println("start date:(mm/dd/yy)");
		String start_date=input.nextLine();
		System.out.println("end date:(mm/dd/yy)");
		String end_date=input.nextLine();
		
		System.out.println("As usual defult value is historical price data. In case we want different data thats "
				+ "why provided the option to choose");
		System.out.println("Other options: put 'div' or 'split' for dividends and split stock splits respectively\n");
		System.out.println("Now insert the option if you like to:");
		String show_option=input.nextLine();
		
		System.out.println("Frequency defult value: daily=1d");
		System.out.println("other option: weekly=1wk, monthly=1mo\n");
		System.out.println("Insert desired frequency:");
		String freq=input.nextLine();
		VolatilityIndexTask.HistoricalPricedataRead(start_date, end_date, show_option, freq,conL);
		System.out.println("\n\n");
		System.out.println("::Call the process of updating the Volatility Index data::");
		VolatilityIndexDataUpdateTask.VolatilityDataUpdateProcess(conL);
		System.out.println("Now inser or update data in portfolio time seies manager");
		
		System.out.println("Enter the start date to insert data:");
		String start_date1 = input.nextLine();

		System.out.println("Last date of data insertion:");
		String end_date1 = input.nextLine();
		ZIndexTask.PortfolioTimeseriesInsert(conL,conKkr,conkkrProd,start_date1,end_date1);
	}

}
