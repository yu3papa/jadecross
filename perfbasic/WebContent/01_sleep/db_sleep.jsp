<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.PropertyReader"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DB Sleep</title>
</head>
<body>
	<%
		/*
		개요 : 파라미터로 넘겨받은 시간만큼 DB에서 sleep
		용도 : DB Connection Pool의 개수 확인 시 사용
		*/
		long startTime = System.currentTimeMillis();
		String sleepSec = request.getParameter("sleepSec");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Context jndiCntx = new InitialContext();
			String dsJndiName = PropertyReader.getInstance().getProperty("jdbc.datasource.jndi.name");
			// DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
			// System.out.println("=============dsJndiName=" + dsJndiName);
			DataSource ds = (DataSource) jndiCntx.lookup(dsJndiName);
			conn = ds.getConnection();

			stmt = conn.prepareStatement("select sysdate, fn_sleep_n(" + sleepSec + ") as sleepTime from dual");
			rs = stmt.executeQuery();

			while (rs.next()) {
	%>
	<%=rs.getString("sysdate") + "," + rs.getString("sleepTime") + "</br>"%>
	<%
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) try { stmt.close(); } catch (Exception e) { }
			if (rs != null) try { rs.close();} catch (Exception e) {}
			if (conn != null) try { conn.close(); } catch (Exception e) { }
		}
		
		long executeMs = System.currentTimeMillis() - startTime;
	%>
	<%= "처리시간 : " + executeMs + "ms" %>
</body>
</html>