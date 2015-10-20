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
			<h1 class="test-result">
				<span class="translate" data-lang-key="Your mark"></span>: <%= result %>
			</h1>
		</div>
		
		<div class="reference-block">
			<h2>
				<span class="translate" data-lang-key="Thanks"></span>! 
				<span class="translate" data-lang-key="You can"></span>
				<a href="<%= basePath %>/logout"><span class="go-logout translate" data-lang-key="sign out"></span></a>
				<span class="translate" data-lang-key="or go to the"></span> 
				<a href="<%= basePath %>"><span class="translate" data-lang-key="main page"></span></a>.
			</h2>
			
		</div>
		
	</div>

<%@ include file="footer.jsp" %>
