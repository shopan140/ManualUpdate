package com.kkr.model.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.kkr.model.CompanyTickerInfo;
import com.kkr.util.DataBaseUtils;



public class UpdateDeleteTickerTask {
	private static String[] exGroupArr = { "DOW", "DJHF", "CBO", "RUS", "BATS", "BZX", "EDGX", "NSD", "NMF", "OTC",
			"OTO", "NYE", "AMX" };
	private static String iStatement = "INSERT INTO `validationdb`.`symbolslist`(`instrumenttype`, `longname`, `shortname`, `symbol`, `exchange`, `symbolstring`, `issuetype`,"
			+ " `sectype`, `isocfi`) VALUES (?,?,?,?,?,?,?,?,?)";

	public static Map<String, Map<Integer, String>> TypeWiseTcikerMapQM = new HashMap<String, Map<Integer, String>>();
	public static Map<String, Map<Integer, String>> TypeWiseTcikerMapDatBase = new HashMap<String, Map<Integer, String>>();
	public static Map<String, String> typeMap = new HashMap<String, String>();
	public static ArrayList<CompanyTickerInfo> tickerinfoQMList = new ArrayList<CompanyTickerInfo>();
	public static ArrayList<CompanyTickerInfo> addList = new ArrayList<CompanyTickerInfo>();
	public static Map<Integer,String> deleteCompanyTickerList = new HashMap<Integer,String>();
	public static Map<String, CompanyTickerInfo> symbolObjectMap = new HashMap<String, CompanyTickerInfo>();
	public static Map<Integer,String>  finalSymbolDeleteList = new HashMap<Integer,String>();


	public static void typeMapCreation(Connection con) throws SQLException {
		String sql = "SELECT DISTINCT Type FROM `kkr_company`";
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			String typeString = rs.getString(1);
			typeMap.put(typeString.toLowerCase(), typeString.toLowerCase());
		}
	}

	public static void companyTickerMapTypeWise(Connection con) throws SQLException {
		for (Map.Entry<String, String> entry : typeMap.entrySet()) {
			String sql = "SELECT kkr_company_id,Company_ticker FROM `kkr_company` WHERE type ='" + entry.getKey() + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			Map<Integer, String> typeTcikerMap = new HashMap<Integer, String>();
			while (rs.next()) {
				Integer company_id = rs.getInt(1);
				String company_ticker = rs.getString(2);
				typeTcikerMap.put(company_id, company_ticker);
			}
			TypeWiseTcikerMapDatBase.put(entry.getKey(), typeTcikerMap);
		}
	}

	private static JSONObject getResultsFromQM(String exGroup) throws IOException {
		System.out.println("http://app.quotemedia.com/data/getSymbols.json?webmasterId=102417&exgroup=" + exGroup);
		URL url = new URL("http://app.quotemedia.com/data/getSymbols.json?webmasterId=102417&exgroup=" + exGroup);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String responseString = "";
		String enterpriseToken = "NWFiNjM2OGEtYjFmNy00YmNiLThlYTktOTQyMjM4ZGJjMGQ1";
		try {
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + enterpriseToken);
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuffer response = new StringBuffer();
			while ((output = br.readLine()) != null) {
				response.append(output);
			}
			responseString = response.toString();
			br.close();
			JSONObject json = new JSONObject(responseString);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void createAllListnMaps(Connection cLocal) throws SQLException {
		// TODO Auto-generated method stub
		typeMapCreation(cLocal);
		companyTickerMapTypeWise(cLocal);
		int k = 0;
		for (String exGroup : exGroupArr) {
			System.out.println("Going to check for exGroup:" + exGroup);
			try {
				JSONObject jsonObject = getResultsFromQM(exGroup);
				JSONObject resultsJson = (JSONObject) jsonObject.get("results");
				JSONArray lookupdataJsonArr = resultsJson.has("lookupdata") ? resultsJson.getJSONArray("lookupdata")
						: null;
				if (lookupdataJsonArr != null) {
					for (int i = 0; i < lookupdataJsonArr.length(); i++) {
						JSONObject lookupDataJson = lookupdataJsonArr.getJSONObject(i);
						JSONObject equityinfoJSON = lookupDataJson.getJSONObject("equityinfo");
						JSONObject keyJSON = lookupDataJson.getJSONObject("key");

						String instrument_type = equityinfoJSON.has("instrumenttype")
								? equityinfoJSON.getString("instrumenttype") : null;
						String longname = equityinfoJSON.has("longname") ? equityinfoJSON.getString("longname") : null;
						String shortname = equityinfoJSON.has("shortname") ? equityinfoJSON.getString("shortname")
								: null;
						String symbol = keyJSON.has("symbol") ? keyJSON.getString("symbol") : null;
						String exchange = keyJSON.has("exchange") ? keyJSON.getString("exchange") : null;
						String symbolstring = lookupDataJson.has("symbolstring")
								? lookupDataJson.getString("symbolstring") : null;
						String issuetype = lookupDataJson.has("symbolstring") ? lookupDataJson.getString("symbolstring")
								: null;
						String sectype = equityinfoJSON.has("sectype") ? equityinfoJSON.getString("sectype") : null;
						String isocfi = equityinfoJSON.has("isocfi") ? equityinfoJSON.getString("isocfi") : null;
						
						if (symbol.contains(":")|| symbol.equals(null)) {
							continue;
						}
						if (typeMap.containsKey(instrument_type.toLowerCase())) {
							CompanyTickerInfo cti = new CompanyTickerInfo();
							cti.setInstrumenttype(instrument_type);
							cti.setSymbol(symbol);
							cti.setLongname(longname);
							cti.setShorname(shortname);
							cti.setExchange(exchange);
							cti.setSymbol_string(symbolstring);
							cti.setIssuetype(issuetype);
							cti.setSectype(sectype);
							cti.setIsocfi(isocfi);
							if (!TypeWiseTcikerMapQM.containsKey(instrument_type)) {
								Map<Integer, String> mm = new HashMap<Integer, String>();
								mm.put(k, symbol);
								TypeWiseTcikerMapQM.put(instrument_type, mm);
								k++;
							} else {
								Map<Integer, String> mm = new HashMap<Integer, String>();
								mm.putAll(TypeWiseTcikerMapQM.get(instrument_type));
								mm.put(k, symbol);
								TypeWiseTcikerMapQM.put(instrument_type, mm);
								k++;
							}
							// tickerinfoQMList.add(cti);
							symbolObjectMap.put(symbol, cti);
							// System.out.println("Successful!!");
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void CreateAddDeleteList(Connection con) throws IOException {

	
		for (Map.Entry<String, Map<Integer, String>> eEntry : TypeWiseTcikerMapDatBase.entrySet()) {
			String instrumentType = eEntry.getKey();
			if (TypeWiseTcikerMapQM.containsKey(instrumentType)) {
				Map<Integer, String> mQM = new HashMap<Integer, String>();
				mQM.putAll(TypeWiseTcikerMapQM.get(instrumentType));
				int k=0;
				for (Map.Entry<Integer, String> eeEntry : eEntry.getValue().entrySet()) {
					String ticker = eeEntry.getValue();
					if (!mQM.containsValue(ticker)) {
						deleteCompanyTickerList.put(eeEntry.getKey(),eeEntry.getValue());
						k++;
					}

				}
				System.out.println("type: " + instrumentType + " :: delete number size: " + k);
			}
		}
		BufferedWriter bw= new BufferedWriter(new FileWriter(new File("resources/deleteList.txt")));
		bw.write(deleteCompanyTickerList.toString());
		//bw.close();
		
		
		for (Map.Entry<String, Map<Integer, String>> eEntry : TypeWiseTcikerMapQM.entrySet()) {
			String instrumentType = eEntry.getKey();
			int m=0;
			if (TypeWiseTcikerMapDatBase.containsKey(instrumentType)) {
				Map<Integer, String> mDataBase = new HashMap<Integer, String>();
				mDataBase.putAll(TypeWiseTcikerMapDatBase.get(instrumentType));
				
				for (Map.Entry<Integer, String> eeEntry : eEntry.getValue().entrySet()) {
					String ticker = eeEntry.getValue();
					if (!mDataBase.containsValue(ticker)) {
						addList.add(symbolObjectMap.get(eeEntry.getValue()));
						m++;
					}
				}
				System.out.println("type: " + instrumentType + " :: add number: " + m);
			}
		}
		BufferedWriter bw1=new BufferedWriter(new FileWriter(new File("resources/addlist.txt")));
		
		for(int i=0;i<addList.size();i++){
			bw1.write(addList.get(i).toString()+"\n \n");
		}
		
		//bw.close();
		System.out.println("QM list: "+symbolObjectMap.size());
		System.out.println("Successfully Commpleted.");
	}

	public void finalDeletListBuild(String startDate,String endDate,Connection cKkr) throws SQLException, IOException, ClassNotFoundException {
		
		for (Map.Entry<Integer, String> entry : deleteCompanyTickerList.entrySet()){
			GetFullPriceHistory gfph=new GetFullPriceHistory();
			try {
				boolean decisionFlag=gfph.finalDeleteList(entry.getValue(), startDate, endDate);
				System.out.println("Iteration: "+entry.getKey()+" ticker: "+entry.getValue()+" Boolean Value : "+decisionFlag);
				if(decisionFlag) {
					finalSymbolDeleteList.put(entry.getKey(),entry.getValue());
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedWriter bw1=new BufferedWriter(new FileWriter(new File("resources/FinalDeletelist.txt")));
		Connection conL=DataBaseUtils.connectLocal();
		for (Map.Entry<Integer, String> entry : finalSymbolDeleteList.entrySet()){
			String SQL="select * from kkr_company where kkr_company_id='"+entry.getKey()+"'";
			PreparedStatement pSLocal = conL.prepareStatement("INSERT INTO kkr_company_delete_list(`kkr_company_id`,`Company_name`,`Company_ticker`,`Type`,"
					+ "`Sector_id`,`Business_description`,`CEO`,`CFO`,`GC`,"
					+ "`Searchable`,`default_index`,`sp500`,`end_date`,`Quarter`,`Short_Name`,`Sec_Type`,`Analyst_recommendation`,"
					+ "`Analyst_recommendation1`,`Analyst_recommendation3`,"
					+ "`Analyst_recommendation4`,`industry_type`,`Qm_sector_name`,`inserted_date`,`updated_date`,`exchange`) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			Statement stKKr=cKkr.createStatement();
			ResultSet rs=stKKr.executeQuery(SQL);
			rs.next();
			Integer company_id=rs.getInt(1);
			String company_name=rs.getString(2);
			String company_ticker=rs.getString(3);
			String type=rs.getString(4);
			Integer sector_id=rs.getInt(5);
			String business_description=rs.getString(6);
			String CEO = rs.getString(7);
			String CFO= rs.getString(8);
			String GC=rs.getString(9);
			String Searchable=rs.getString(10);
			Integer default_index=rs.getInt(11);
			String sp500=rs.getString(12);
			String end_date=rs.getString(13);
			Integer quarter=rs.getInt(14); 
			String short_name=rs.getString(15);
			String sec_type=rs.getString(16);
			String Analyst_recommendation=rs.getString(17);
			String Analyst_recommendation1=rs.getString(18);
			String Analyst_recommendation3=rs.getString(19);
			String Analyst_recommendation4=rs.getString(20);
			String industry_type=rs.getString(21);
			String Qm_sector_name=rs.getString(22);
			String inserted_date=rs.getString(23);
			String updated_date=rs.getString(24);
			String exchange=rs.getString(25);
			
			pSLocal.setInt(1, company_id);
			pSLocal.setString(2, company_name);
			pSLocal.setString(3, company_ticker);
			pSLocal.setString(4, type);
			pSLocal.setInt(5, sector_id);
			pSLocal.setString(6, business_description);
			pSLocal.setString(7, CEO);
			pSLocal.setString(8, CFO);
			pSLocal.setString(9, GC);
			pSLocal.setString(10, Searchable);
			pSLocal.setInt(11, default_index);
			pSLocal.setString(12, sp500);
			pSLocal.setString(13, end_date);
			pSLocal.setInt(14, quarter);
			pSLocal.setString(15, short_name);
			pSLocal.setString(16, sec_type);
			pSLocal.setString(17, Analyst_recommendation);
			pSLocal.setString(18, Analyst_recommendation1);
			pSLocal.setString(19, Analyst_recommendation3);
			pSLocal.setString(20, Analyst_recommendation4);
			pSLocal.setString(21, industry_type);
			pSLocal.setString(22, Qm_sector_name);
			pSLocal.setString(23, inserted_date);
			pSLocal.setString(24, updated_date);
			pSLocal.setString(25, exchange);
			
			pSLocal.execute();
			
			
			bw1.write(entry.getKey()+" "+entry.getValue().toString()+"\n \n");
		}
		System.out.println("refined list: "+finalSymbolDeleteList.size());
	}
	public void InsertData(Connection ckkr) throws SQLException{
		PreparedStatement ps=ckkr.prepareStatement("INSERT INTO kkr_company(`Company_name`,`Company_ticker`,`Type`,`Short_Name`,`Sec_Type`,`exchange`) VALUES(?,?,?,?,?,?)");
		for(int i=0;i<addList.size();i++){
			ps.setString(1, addList.get(i).getLongname());
			ps.setString(2, addList.get(i).getSymbol());
			ps.setString(3, addList.get(i).getInstrumenttype());
			ps.setString(4, addList.get(i).getShorname());
			ps.setString(5, addList.get(i).getSectype());
			ps.setString(6,addList.get(i).getExchange());
			
			ps.execute();
			
		}
	}
	public void DeleteFromTable(Connection con,Connection cKkrClient) throws SQLException{
		DeleteTickerTask dtt=new DeleteTickerTask();
		dtt.deleteCallFunctions(finalSymbolDeleteList,con,cKkrClient);
	}
}
