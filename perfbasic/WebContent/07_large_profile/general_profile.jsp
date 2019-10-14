<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.PropertyReader"%>

<html>
<head>
	<title>General Profile</title>
</head>
<body>
    <h1>일반 사이즈의 트랜잭션 프로파일 - APM 수집서버의 성능 관찰용</h1>
    <br>
    <%
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select DEPTNO, DNAME, LOC from DEPT";

        try {
            Context jndiCntx = new InitialContext();
            DataSource ds = (DataSource) jndiCntx.lookup("java:comp/env/jdbc/scott");
            conn = ds.getConnection();

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
