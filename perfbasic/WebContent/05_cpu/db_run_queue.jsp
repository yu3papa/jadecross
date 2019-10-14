<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>DB CPU Load</title>
</head>
<%
	// parameter 가져오기
	int dbLoopCount = Integer.parseInt(request.getParameter("count")); // DB에서 다중 For를 도는 카운트 1000 정도가 적합함

	// ########## 02. DB에서 CPU 사용률 증가 START ##########
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	try {
		Context jndiCntx = new InitialContext();
		String dsJndiName = PropertyReader.getInstance().getProperty("jdbc.datasource.jndi.name");
		//DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
		DataSource ds = (DataSource) jndiCntx.lookup(dsJndiName);
		
		conn = ds.getConnection();
		stmt = conn.prepareStatement("select sysdate, fn_cpu_waste_n(" + dbLoopCount + ") as loopCount from dual");
		rs = stmt.executeQuery();

		while (rs.next()) {
			rs.getString("sysdate");
			rs.getString("loopCount");
		}
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		if (stmt != null) try { stmt.close(); } catch (Exception e) { }
		if (rs != null) try { rs.close(); } catch (Exception e) { }
		if (conn != null) try { conn.close(); } catch (Exception e) { }
	}
	// ########## 02. DB에서 CPU 사용률 증가 END ##########
%>
<body>
<h1>DBMS CPU 부하발생</h1>
</body>
</html>