package com.kkr.model.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DeleteTickerTask {
	
	public static String delete_portfolio_benchmark="delete from kkrClient.portfolio_benchmark where symbol=";
	public static String delete_portfolio_ticker="delete from kkrClient.portfolio_ticker where symbol=";
	public static String delete_user_favourite="delete from kkrClient.user_favourite where user_favourite.data=";
	public static String delete_user_home_graph_history="delete from kkrClient.user_home_graph_history where symbol=";
	
	public static String delete_default_portfolio_ticker="delete from kkrdb.default_portfolio_ticker where symbol=";
	public static String delete_fund_asset_allocations="delete from kkrdb.fund_asset_allocations where Fund_symbol=";
	public static String delete_fund_capital_allocation="delete from kkrdb.fund_capital_allocation where Fund_symbol=";
	public static String delete_fund_countrybreakdown="delete from kkrdb.fund_countrybreakdown where Fund_symbol=";
	public static String delete_fund_daily_info="delete from kkrdb.fund_daily_info where companyId=";
	public static String delete_fund_equity_sector_allocation="delete from kkrdb.fund_equity_sector_allocation where Fund_symbol=";
	public static String delete_fund_maturity_breakdown="delete from kkrdb.fund_maturity_breakdown where Fund_symbol=";
	public static String delete_fund_regional_exposure="delete from kkrdb.fund_regional_exposure where Fund_symbol";
	public static String delete_fund_top10holding_raw="delete from kkrdb.fund_top10holding_raw where Fund_symbol=";
	public static String delete_kkr_analyst_ratings="delete from kkrdb.kkr_analyst_ratings where Symbol="; 
	public static String delete_kkr_balanceSheet_raw="delete from kkrdb.kkr_balanceSheet_raw where Symbol=";
	public static String delete_kkr_benchmark_data="delete from kkrdb.kkr_benchmark_data where KKR_COMPANY_ID="; 
	public static String delete_kkr_benchmark_data_current="delete from kkrdb.kkr_benchmark_data_current where KKR_COMPANY_ID";
	public static String delete_kkr_cashflow_raw="delete from kkrdb.kkr_cashflow_raw where Symbol=";
	public static String delete_kkr_company_management="delete from kkrdb.kkr_company_management where KKR_COMPANY_ID=";
	public static String delete_kkr_company_raw="delete from kkrdb.kkr_company_raw where Symbol=";
	public static String delete_KKR_FUND_RAW="delete from kkrdb.KKR_FUND_RAW where Fund_symbol=";
	public static String delete_kkr_fund_mgr_Raw="delete from kkrdb.kkr_fund_mgr_Raw where Fund_symbol=";
	public static String delete_kkr_fundPerform_raw="delete from kkrdb.kkr_fundPerform_raw where Fund_symbol=";
	public static String delete_kkr_incomestatement_raw="delete from kkrdb.kkr_incomestatement_raw where Symbol=";
	public static String delete_kkr_interim_raw="delete from kkrdb.kkr_interim_raw where Symbol=";
	public static String delete_kkr_market_data="delete from kkrdb.kkr_market_data where KKR_COMPANY_ID=";
	public static String delete_kkr_market_data_current="delete from kkrdb.kkr_market_data_current where KKR_COMPANY_ID=";
	public static String delete_Kkr_mptriskstatistics_raw="delete from kkrdb.Kkr_mptriskstatistics_raw where Fund_symbol=";
	public static String delete_kkr_option_price="delete from kkrdb.kkr_option_price where kkr_option_id=";
	public static String delete_kkr_option_data="delete from kkrdb.kkr_option_data where kkr_company_id=";
	public static String delete_Kkr_sharholdingemp_raw="delete from kkrdb.Kkr_sharholdingemp_raw where Symbol=";
	public static String delete_review_details="delete from kkrdb.review_details where company_ticker=";
	public static String delete_zsenia_correlation_prices="delete from kkrdb.zsenia_correlation_prices where company_ticker=";
	public static String delete_zsenia_correlation_results="delete from kkrdb.zsenia_correlation_results where ticker1=";
	public static String delete_zsenia_correlation_results1="delete from kkrdb.zsenia_correlation_results where ticker2=";
	public static String delete_zsenia_correlation_returns="delete from kkrdb.zsenia_correlation_returns where company_ticker=";
	public static String delete_zsenia_correlation_tickers="delete from kkrdb.zsenia_correlation_tickers where company_ticker=";
	public static String delete_zsenia_equity_financials="delete from kkrdb.zsenia_equity_financials where company_ticker=";
	public static String delete_zsenia_fund_dividends="delete from kkrdb.zsenia_fund_dividends where company_ticker=";
	public static String delete_zsenia_kkr_company_raw_quarter="delete from kkrdb.zsenia_kkr_company_raw_quarter where symbol=";
	public static String delete_kkr_price="delete from kkrdb.kkr_price where KKR_company_ID=";
	public static String delete_kkr_company="delete from kkrdb.kkr_company where kkr_company_id=";
	
	public void deleteCallFunctions(Map<Integer,String> mainDeleteList,Connection con,Connection cKkrClient) throws SQLException{
		for(Map.Entry<Integer, String> entry:mainDeleteList.entrySet()){
			PreparedStatement pSLocal = cKkrClient.prepareStatement(delete_portfolio_benchmark+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=cKkrClient.prepareStatement(delete_portfolio_ticker+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=cKkrClient.prepareStatement(delete_user_favourite+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=cKkrClient.prepareStatement(delete_user_favourite+entry.getValue());
			pSLocal.executeQuery();
			
			pSLocal=con.prepareStatement(delete_default_portfolio_ticker+entry.getValue());
			pSLocal.executeQuery();			
			pSLocal=con.prepareStatement(delete_fund_asset_allocations+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_capital_allocation+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_countrybreakdown+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_daily_info+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_equity_sector_allocation+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_maturity_breakdown+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_regional_exposure+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_fund_top10holding_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_analyst_ratings+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_balanceSheet_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_benchmark_data+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_benchmark_data_current+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_cashflow_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_company_management+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_company_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_KKR_FUND_RAW+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_fund_mgr_Raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_fundPerform_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_incomestatement_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_interim_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_market_data+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_market_data_current+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_Kkr_mptriskstatistics_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_option_price+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_option_data+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_Kkr_sharholdingemp_raw+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_review_details+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_correlation_prices+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_correlation_prices+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_correlation_results+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_correlation_results1+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_correlation_returns+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_equity_financials+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_fund_dividends+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_zsenia_kkr_company_raw_quarter+entry.getValue());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_price+entry.getKey());
			pSLocal.executeQuery();
			pSLocal=con.prepareStatement(delete_kkr_company+entry.getValue());
			pSLocal.executeQuery();
		}
		
	}

}
