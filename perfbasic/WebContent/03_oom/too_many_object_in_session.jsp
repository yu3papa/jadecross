<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.jadecross.perflab.oom.Member"%>

<%
	long startTime = System.currentTimeMillis();

	ArrayList<Member> members;
	Member member = new Member();
	members = (ArrayList<Member>) request.getSession().getAttribute("members");
	if (members == null) {
		members = new ArrayList<Member>();
		request.getSession().setAttribute("members", members);
	}

	members.add(new Member());
	long executeMs = System.currentTimeMillis() - startTime;
%>
<%="member count=" + members.size()%></br>
<%="WAS 처리시간 : " + executeMs + "ms"%>