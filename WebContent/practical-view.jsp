<%@page import="java.io.File"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.PracticalsBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% PracticalsBean pBean = (PracticalsBean) request.getAttribute("practicalBean"); %>
<% String body = pBean.getBody(); %>
<% String fileName = pBean.getFileName(); %>
<% String fileExt = (fileName.lastIndexOf(".") > 0) ? fileName.substring(fileName.lastIndexOf(".") + 1) : "_blank"; %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
	<div class="alert alert-${status}">
		<p>${message}</p>
	</div>
	<% } %>
</div>

<div class="container">
	<h1 class="lead"><%= pBean.getSubject() %></h1>
	<h2><%= pBean.getTitle() %></h2>
	<% if (!body.isEmpty()) { %>
		<div class="body"><%= body %></div>
	<% } %>
	<% if (!fileName.isEmpty()) { %>
		<div class="file">
			<a href="<%= saveDir + File.separator + fileName %>">
			<img src="imgs/icons/<%= fileExt %>.png" alt="<%= fileName %>" />
			<%= fileName %>
			</a>
		</div>
	<% } %>
</div>

<%@ include file="footer.jsp" %>
