<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	/*
	개요 : 파라미터로 넘겨받은 count만큼 Loop를 돌면서 CPU 소모할 때 RunQueue의 변화량과 응답시간 지연을 관찰
	*/
	long startTime = System.currentTimeMillis();
	int count;
	double result;

	startTime = System.currentTimeMillis();
	count = Integer.parseInt(request.getParameter("count"));

	for (int i = 1; i < count; i++) {
		result = i * Math.random() / count;
		for (int j = 1; j < count; j++) {
			result = i * j * Math.random() / count;

			for (int k = 1; k < count; k++) {
				// CPU 만 소모
			}
		}
	}
	long executeMs = System.currentTimeMillis() - startTime;
%>
<%="WAS 처리시간 : " + executeMs + "ms"%>