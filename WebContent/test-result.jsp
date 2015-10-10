<%@page import="beans.TestBean"%>
<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% int result = (int) request.getAttribute("result"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		
		<div class="page-header">
			<h1 class="test-result">Кількість балів: <%= result %></h1>
		</div>
		
	</div>

<%@ include file="footer.jsp" %>
