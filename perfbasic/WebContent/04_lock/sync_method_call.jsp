<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%!public static synchronized void sleep(long sleepMilisec) {
		try {
			if (sleepMilisec >= 1000) {
				Thread.sleep(sleepMilisec);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}%>

<%
	long startTime = System.currentTimeMillis();
	long sleepMiliSec = Long.parseLong(request.getParameter("sleepMiliSec"));

	sleep(sleepMiliSec);

	long executeMs = System.currentTimeMillis() - startTime;
%>
<%="WAS 처리시간 : " + executeMs + "ms"%>