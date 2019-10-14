<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.nio.channels.*"%>
<%@ page import="java.util.*"%>

<%
	String contextRoot = getServletContext().getRealPath("/");
	String newJspName = "C_" + UUID.randomUUID().toString().replace("-", "") + ".jsp";

	// 01. 신규 JSP 파일 생성
	fileCopy(contextRoot + "03_oom/jdbc_sample.jsp", contextRoot + "03_oom/temp_jsp/" + newJspName);

	// 02. 신규 JSP 파일로 이동
	request.getRequestDispatcher("temp_jsp/" + newJspName).forward(request, response);
%>

<%!public void fileCopy(String orgFilePath, String newFilePath) {
		File orgFile = new File(orgFilePath);
		try {
			FileInputStream inputStream = new FileInputStream(orgFile);
			FileOutputStream outputStream = new FileOutputStream(newFilePath);
			FileChannel fcin = inputStream.getChannel();
			FileChannel fcout = outputStream.getChannel();

			long size = fcin.size();
			fcin.transferTo(0, size, fcout);

			fcout.close();
			fcin.close();
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {
		}
	}%>