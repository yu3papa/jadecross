<%@ page session="true"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.jadecross.*"%>
<%!public Connection getConnection() throws Exception {

		Context jndiCntx = new InitialContext();
		String dsJndiName = PropertyReader.getInstance().getProperty("jdbc.datasource.jndi.name");
		//DataSource datasource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/scott");
		DataSource datasource = (DataSource) jndiCntx.lookup(dsJndiName);
		return datasource.getConnection();
	}

	Random rand = new Random();%>
<html>
<body>
	<%
		Thread.sleep(rand.nextInt(1000));

		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from s_scouter");
		while (rs.next()) {
			String id = rs.getString(1);
			String name = rs.getString(2);
			out.println(id + " " + name + "<br>");
		}
		rs.close();
		stmt.close();
		conn.commit();
		conn.close();
	%>
	This is e2end.jsp
</body>
</html>
