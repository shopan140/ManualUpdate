package com.kkr.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class DataBaseUtils {
	
	
	
	public static Connection connectLocal() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://localhost:3306/kkr_database?rewriteBatchedStatements=true", "root", "Orchid1406");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectKkrClient() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://localhost:3306/kkrclient?rewriteBatchedStatements=true", "root", "");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectkkrProd() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		/*
		 * connection string
		 * "jdbc:mysql://kkrprod.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true"
		 * ,"kkr_app","kkr123"
		 * 
		 */
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://kkrprod.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true","kkr_app","kkr123");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectkkrProdClient() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		/*
		 * connection string
		 * "jdbc:mysql://kkrprod.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true"
		 * ,"kkr_app","kkr123"
		 * 
		 */
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://kkrprod.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrClient?rewriteBatchedStatements=true","kkr_app","kkr123");
		con.setAutoCommit(true);
		return con;
	}

	public static Connection connectkkrDevClient() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		/*
		 * Connection string for kkrdev
		 * "jdbc:mysql://kkrdev.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true"
		 * ,"kkr_app","kkr123"
		 */
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://kkrdev.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrClient?rewriteBatchedStatements=true","kkr_app","kkr123");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectkkrDev() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		/*
		 * Connection string for kkrdev
		 * "jdbc:mysql://kkrdev.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true"
		 * ,"kkr_app","kkr123"
		 */
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://kkrdev.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true","kkr_app","kkr123");
		con.setAutoCommit(true);
		return con;
	}
	
	public static Connection connectsislam() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		/*
		 * Connection string for kkrdev
		 * "jdbc:mysql://kkrdev.craeiofbogb9.us-west-2.rds.amazonaws.com:3306/kkrdb?rewriteBatchedStatements=true"
		 * ,"kkr_app","kkr123"
		 */
		Connection con = (Connection) DriverManager
				.getConnection("jdbc:mysql://192.168.0.14:3306/kkrdb?rewriteBatchedStatements=true","shopan140","123");
		con.setAutoCommit(true);
		return con;
	}
	
	
	public static  boolean createBackup(String tableName, Connection con, String specialConditions) throws Exception {

		Statement stmt = null;	
		String createBackupQuery = "create table zsenia_backup.Z_bkp_<DATE>_<TABLE_NAME>  as select * from <TABLE_NAME>";
		String query = createBackupQuery.replace("<DATE>", DateUtils.formatDate(new Date(), "ddMMyy_HH_MM_SS"));
		query=query.replace("<TABLE_NAME>", tableName);
		if(specialConditions!=null && !specialConditions.isEmpty()) {
			query=query+" "+specialConditions;
		}
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		return true;
	}
	
	public static boolean truncateTable(String tableName, Connection con) throws Exception {

		Statement stmt = null;
		String truncateTableQuery = "truncate table <TABLE_NAME>";
		String query = truncateTableQuery.replace("<TABLE_NAME>", tableName);
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		return true;
	}
}
