package com.kkr.app;

import java.sql.Connection;
import java.sql.SQLException;

import com.kkr.model.task.CompanyMissingInfoTask;
import com.kkr.util.DataBaseUtils;

public class CompanyMissingInfoApp {

	public static void main(String []args) throws ClassNotFoundException, SQLException{
		//Connection conKkrDev = DataBaseUtils.connectkkrDev();
		Connection conKkrProd = DataBaseUtils.connectkkrProd();
		//Connection conL=DataBaseUtils.connectLocal();
		
		System.out.println("Start Company missing check and insert and update accordingly: ");
		CompanyMissingInfoTask cmit= new CompanyMissingInfoTask();
		//cmit.CompanyMissingCheckUpdate(conKkrDev);
		cmit.CompanyMissingCheckUpdate(conKkrProd);
	}
}
