<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	/*
	개요 : 파라미터로 넘겨받은 시간만큼 WAS에서 sleep
	용도 : Thread Pool의 개수 확인 시 사용
	*/
 	long startTime = System.currentTimeMillis();
	String sleepMiliSec = request.getParameter("sleepMiliSec");

	try {
		//Thread.sleep(1000);
		Thread.sleep(Integer.parseInt(sleepMiliSec));
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	long executeMs = System.currentTimeMillis() - startTime;
%>
<%= "WAS 처리시간 : " + executeMs + "ms" %>