<%@ page session="true"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.*"%>
<html>
<body>
	<%!public Connection getConnection() throws Exception {

		Context jndiCntx = new InitialContext();
		String dsJndiName = PropertyReader.getInstance().getProperty("jdbc.datasource.jndi.name");
		//DataSource datasource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/scott");
		DataSource datasource = (DataSource) jndiCntx.lookup(dsJndiName);
		return datasource.getConnection();
	}

	private int getTime(String t) {
		if (t == null)
			return 8000;
		return Integer.parseInt(t);
	}%>

	<%
		int t = getTime(request.getParameter("t"));
		Connection conn = getConnection();
		conn.setAutoCommit(false);

		Statement stmt = conn.createStatement();
		stmt.executeUpdate("update emp set ename='SMITH' where empno=7369");

		Thread.sleep(t);

		conn.commit();
		stmt.close();
		conn.close();
	%>
</body>
</html>