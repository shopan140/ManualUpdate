package com.kkr.model.task;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kkr.model.VolatilityIndex;
import com.kkr.util.DataBaseUtils;
import com.kkr.util.DateUtils;

public class VolatilityIndexTask {
	public static final SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yy");
	public static final long ONE_DAY_IN_SECS = 86400;



	public static boolean HistoricalPricedataRead(String start_date, String end_date, String show_option, String freq,Connection con)
			throws Exception {

		int countData=0;
		long end_secs = 0;
		long start_secs = 0;
		String interval=null;
		String filter=null;
		String frequency=null;
		String base_URL="https://finance.yahoo.com/quote/%5EVIX/";
		String urlPeriod1 = "history?period1=";
		String urlPeriod2 = "&period2=";
		String urlInterval="&interval=";
		String urlfilter="&filter=";
		String urlfrequency="&frequency=";
		
		try {
			// fetch the document over HTTP
			Document doc = null;
			if (show_option.equals("") && freq.equals("") && start_date.equals("") && end_date.equals("")) {
				
				doc = Jsoup.connect("https://finance.yahoo.com/quote/%5EVIX/history?p=%5EVIX").get();
			} else {
				/*
				 * calculating period 1 and period 2 values from date options
				 */
				if (end_date.equals("")) {
					Date cur_date = new Date();
					end_secs = cur_date.getTime() / 1000 + ONE_DAY_IN_SECS;
				} else {
					Date input_date = dateformat.parse(end_date);
					end_secs = input_date.getTime() / 1000 + ONE_DAY_IN_SECS;
				}
				if (start_date.equals("")) {
					Calendar now = Calendar.getInstance();
					now.add(Calendar.YEAR, -1);
					String prev_date = dateformat.format(now.getTime());
					Date s_date = dateformat.parse(prev_date);
					start_secs = s_date.getTime() / 1000 + ONE_DAY_IN_SECS;
				} else {
					Date input_date = dateformat.parse(start_date);
					start_secs = input_date.getTime() / 1000 + ONE_DAY_IN_SECS;
				}
				/*
				 * filter setting for historical data
				 */
				if(show_option.equals("")){
					filter="history";
				}
				else{
					filter=show_option;
					interval="div%7Csplit";
				}
				/*
				 * checking whether this is historical data or not 
				 * for historical data frequency and interval are always same
				 * but other than histrical price data for dividends and split 
				 * interval is const value = "div%7Csplit" so only we need to set frequency
				 */
				if(freq.equals("") && filter.equals("history")){
					interval="1d";
					frequency="1d";		
				}
				else if(freq.equals("") && !filter.equals("history")){
					frequency="1d";
				}
				else if(!filter.equals("history")){
					frequency=freq;
				}
				else{
					interval=freq;
					frequency=freq;
				}
				/*
				 * https://finance.yahoo.com/quote/%5EVIX/history?period1=1526925600&period2=1526925600&interval=1d&filter=history&frequency=1d
				 */
				String desired_url=base_URL+urlPeriod1+start_secs+urlPeriod2+end_secs+urlInterval+interval+urlfilter+filter+urlfrequency+frequency;
				doc = Jsoup.connect(desired_url).get();
				
			}

			// get the page title
			String title = doc.title();
			System.out.println("title: " + title);

			Elements table_tag = doc.select("table");
			// get all links in page
			Elements links = doc.select("a[href]");
			System.out.println("table size: "+table_tag.size());
			ArrayList<VolatilityIndex> downServers = new ArrayList<VolatilityIndex>();
			Element table = doc.select("table").get(0); // select the first
														// table.
			Elements rows = table.select("tr");

			for (int i = 1; i < rows.size(); i++) { // first row is the col
				Element row = rows.get(i);
				Elements cols = row.select("td");
				String txt=cols.get(0).text();
				if(txt.contains("Close price adjusted for splits.")){
					break;
				}
				VolatilityIndex vi=new VolatilityIndex();
				vi.setHistorydatadate(DateUtils.stringTodate(cols.get(0).text(),"MMMM d, yy","yyyy-MM-dd"));
				vi.setOpen(Double.parseDouble(cols.get(1).text()));
				vi.setHigh(Double.parseDouble(cols.get(2).text()));
				vi.setLow(Double.parseDouble(cols.get(3).text()));
				vi.setClose(Double.parseDouble(cols.get(4).text()));
				vi.setAdjClose(Double.parseDouble(cols.get(5).text()));
				
				downServers.add(vi);

			}
			countData=InsertDataIntoVolatilityTable(downServers,con,countData);
			if(countData>0) return true;

		} catch (IOException e) {
			e.printStackTrace();
		}	
		return false;
	}
	
	private static int InsertDataIntoVolatilityTable(ArrayList<VolatilityIndex> downServers,Connection con,int countdata) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		

		for (int i = 0; i < downServers.size(); i++) {
			
			String SQL_QUERY = "SELECT * FROM Volatility_Index WHERE history_date ='"
					+ downServers.get(i).getHistorydatadate() + "'";
			Statement checkstatement = con.createStatement();
			ResultSet checkSet = checkstatement.executeQuery(SQL_QUERY);
			int size = 0;
			if (checkSet != null) {
				checkSet.beforeFirst();
				checkSet.last();
				size = checkSet.getRow();
			}

			if (size < 1) {
				System.out.println(SQL_QUERY);
				PreparedStatement pSLocal = con.prepareStatement(
						"insert into Volatility_Index (history_date,Open,High,Low,Close,AdjClose) "
						+ "values (?,?,?,?,?,?)");

				pSLocal.setString(1, downServers.get(i).getHistorydatadate());
				pSLocal.setDouble(2, downServers.get(i).getOpen());
				
				pSLocal.setDouble(3, downServers.get(i).getHigh());
				pSLocal.setDouble(4, downServers.get(i).getLow());
				pSLocal.setDouble(5, downServers.get(i).getClose());
				pSLocal.setDouble(6, downServers.get(i).getAdjClose());
				
				pSLocal.execute();
				countdata++;
			}
		}
		return countdata;
	}
	
}
