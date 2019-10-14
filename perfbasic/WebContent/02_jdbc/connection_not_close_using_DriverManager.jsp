<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="com.jadecross.PropertyReader"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Connection Not Close using DriverManager</title>
</head>
<body>
	<h1> Connection Not Close using DriverManager DEMO</h1><br>
	
<%
	long startTime = System.currentTimeMillis();

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	try {
		Class.forName("oracle.jdbc.OracleDriver");
		String jdbcURL = PropertyReader.getInstance().getProperty("jdbc.url");
		String jdbcUSER = PropertyReader.getInstance().getProperty("jdbc.user");
		String jdbcPWD = PropertyReader.getInstance().getProperty("jdbc.password");
		// conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.8.100:1521:orcl", "sys as sysdba", "jadecross");
		
		conn = DriverManager.getConnection(jdbcURL, jdbcUSER, jdbcPWD);

		stmt = conn.prepareStatement("select DEPTNO, DNAME, LOC from DEPT");
		rs = stmt.executeQuery();

		while (rs.next()) {
%>
<%=rs.getInt("DEPTNO") + "," + rs.getString("DNAME") + "," + rs.getString("LOC") + "</br>"%>
<%
	}
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		if (stmt != null) try { stmt.close();} catch (Exception e) {}
		if (rs != null) try { rs.close();} catch (Exception e) {}
		//if (conn != null) try { conn.close();} catch (Exception e) {}
	}
	long executeMs = System.currentTimeMillis() - startTime;
%>

<%="WAS 처리시간 : " + executeMs + "ms"%>
</body>
</html>