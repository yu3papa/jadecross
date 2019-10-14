<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/netfunnel.js" charset="UTF-8"></script>
<title>채권전자입찰 결과</title>
</head>
<%
	// parameter 가져오기
	String id = request.getParameter("id"); // id를 가지고 Express 가 가능하도록 설정
	String company = request.getParameter("company");
	String amount = request.getParameter("amount");
	int wasLoopCount = Integer.parseInt(request.getParameter("wasLoopCount")); // WAS에서 다중 For를 도는 카운트 1000정도가 적합함
	int dbLoopCount = Integer.parseInt(request.getParameter("dbLoopCount")); // DB에서 다중 For를 도는 카운트 1000정도가 적합함

	// ########## 01. WAS에서 CPU 사용률 증가 START ##########
	int was_sum = 10;
	for (int i = 0; i < wasLoopCount; i++) {
		for (int j = 0; j < wasLoopCount; j++) {
			if (j % 2 == 0) {
				was_sum = was_sum + j;
			} else {
				was_sum = was_sum - j;
			}
		}
	}
	// ########## 01. WAS에서 CPU 사용률 증가 END ##########

	// ########## 02. DB에서 CPU 사용률 증가 START ##########
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	try {
		Context jndiCntx = new InitialContext();
		DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
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
<a href="javascript:history.back();"><img src="img/kcdc.png"></a>
	<h2><font color="blue"><%= id%>(<%= company%>)</font>님. 연구과제가 등록되었습니다.<br/>
	신청연구금액 : <font color="blue"><%= amount%>만원</font></h2>
</body>
</html>