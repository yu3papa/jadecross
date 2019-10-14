package com.jadecross.dic.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
	private static DBConnectionManager _instance = new DBConnectionManager();

	private DBConnectionManager() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static DBConnectionManager getInstance() {
		return _instance;
	}

	/**
	 * OracleIP에 해당하는 Connection 객체 리턴
	 * @return
	 */
	public Connection getConnection(String oracleIP) {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+oracleIP+":1521:orcl" , "jpetstore", "jadecross");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
}
