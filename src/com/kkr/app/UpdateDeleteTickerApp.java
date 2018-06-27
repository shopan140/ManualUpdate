package com.kkr.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kkr.model.task.UpdateDeleteTickerTask;
import com.kkr.util.DataBaseUtils;

public class UpdateDeleteTickerApp {
	public static void main(String[] args) throws SQLException, IOException {
		Connection cLocal = null;
		PreparedStatement pSLocal = null;
		try {
			cLocal = DataBaseUtils.connectLocal();
			//pSLocal = cLocal.prepareStatement(iStatement);
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		// TODO Auto-generated method stub
		UpdateDeleteTickerTask.createAllListnMaps(cLocal);
		UpdateDeleteTickerTask.CreateAddDeleteList(cLocal);
	}
}
