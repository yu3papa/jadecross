<%@ page session="true"%>
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

	public void make(String table) throws Exception {

		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		try {
			stmt.executeUpdate("drop table " + table);
		} catch (Exception e) {
		}
		stmt.executeUpdate("CREATE TABLE " + table + " ( id varchar(40) ,name varchar(40) )");
		for (int i = 0; i < 10000; i++) {
			stmt.executeUpdate("insert into " + table + "(id,name) values('id" + i + "','name" + i + "')");
		}
		stmt.close();
		conn.close();
	}%>
	<%
		make("s_scouter");
		make("s_master");
		make("s_dept");
		make("s_emp");
	%>
	create ok
</body>
</html>
