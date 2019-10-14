package com.jadecross.dic.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DicDAO {
	private String oracleIP;
	
	public DicDAO(String oracleIP){
		this.oracleIP = oracleIP;
	}

	public String getDescription(String word) {
		String retDescription = "단어가 사전에 없습니다";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionManager.getInstance().getConnection(this.oracleIP);

			pstmt = conn.prepareStatement("SELECT DESCRIPTION FROM DICTIONARY WHERE WORD = ?");
			pstmt.setString(1, word);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				retDescription = rs.getString("DESCRIPTION");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) { }
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) { }
			if (conn != null) try { conn.close(); } catch (Exception e) { }
		}

		return retDescription;
	}
}
