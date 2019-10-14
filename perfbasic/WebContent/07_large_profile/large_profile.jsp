<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.PropertyReader"%>

<html>
<head>
	<title>Large Profile</title>
</head>
<body>
<h1>SQL을 여러번 호출할때 바인드변수값이 많이 발생하고, APM에서 수집하는 양이 많아지는데 이때 APM 수집서버의 성능 관찰용</h1>
<h2>전달받은 count만큼 SQL 수행</h2>
<ul>
    <li>바인드 변수 : 50개</li>
    <% int count = Integer.parseInt(request.getParameter("count")); %>
    <li>SQL 호출수 : <%=count%></li>
</ul>
<%
    Connection conn = null; 
	PreparedStatement pstmt = null; 
	ResultSet rs = null;
    String sql = null;

    try {
        Context jndiCntx = new InitialContext();
        DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
        conn = ds.getConnection();
        sql = "select DEPTNO, DNAME, LOC from DEPT where "
        	      + "DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? "
        	      + "or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? "
        	      + "or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? "
        	      + "or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? "
        	      + "or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? or DNAME=? ";

        for (int i = 0; i < count; i++) {
            pstmt = conn.prepareStatement(sql);
            // 50개의 바인드 변수 설정
            for (int j = 1; j <= 50; j++) {
            	pstmt.setString(j, "param" + j + "_" + i);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) { 
            	// System.out.print("."); 
    		}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) { }
            if (rs != null) try { rs.close(); } catch (Exception e) { }
    	}
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        if (pstmt != null) try { pstmt.close(); } catch (Exception e) { }
        if (rs != null) try { rs.close(); } catch (Exception e) { }
        if (conn != null) try { conn.close(); } catch (Exception e) { }
    }
%>
</br>
<%
	// 대용량 프로파일 이후 수행되는 스텝도 수집되는지 확인용
        try {
            Context jndiCntx = new InitialContext();
            DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
            conn = ds.getConnection();
            sql = "select DEPTNO, DNAME, LOC from DEPT";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
    %>
    <%=rs.getInt("DEPTNO") + "," + rs.getString("DNAME") + "," + rs.getString("LOC") + "</br>"%>
    <%
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) { }
            if (rs != null) try { rs.close(); } catch (Exception e) { }
            if (conn != null) try { conn.close(); } catch (Exception e) { }
        }
    %>

</body>
</html>


