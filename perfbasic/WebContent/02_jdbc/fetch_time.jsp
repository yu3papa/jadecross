<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.PropertyReader"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Connection Not Close</title>
</head>
<body>
	<h1> Fetch Time DEMO</h1><br>
	<%
		/*
		개요 : Connection Not Close 시 DB와 WAS의 상황을 관찰한다.
		*/
		long startTime = System.currentTimeMillis();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Context jndiCntx = new InitialContext();
			String dsJndiName = PropertyReader.getInstance().getProperty("jdbc.datasource.jndi.name");
			// DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
			DataSource ds = (DataSource) jndiCntx.lookup(dsJndiName);
			conn = ds.getConnection();

			stmt = conn.prepareStatement("select DEPTNO, DNAME, LOC from DEPT");
			rs = stmt.executeQuery();

			while (rs.next()) {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
	%>
	<%=rs.getInt("DEPTNO") + "," + rs.getString("DNAME") + "," + rs.getString("LOC") + "</br>"%>
	<%
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) try { stmt.close();} catch (Exception e) {}
			if (rs != null) try { rs.close();} catch (Exception e) {}
			if (conn != null) try { conn.close();} catch (Exception e) {}
		}
		long executeMs = System.currentTimeMillis() - startTime;
	%>
	
	<%="WAS 처리시간 : " + executeMs + "ms"%>
</body>
</html>