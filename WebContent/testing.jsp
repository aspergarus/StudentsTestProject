<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<QuestionBean> questions = (ArrayList<QuestionBean>) request.getAttribute("questions"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>

		<form action="<%= basePath %>/testing/" id="testing-form" method="post" class="form-horizontal" >
			
			<h1>Hello from testing.jsp</h1>
		
		</form>
		
	</div>
	
<%@ include file="footer.jsp" %>
