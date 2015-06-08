<%@page import="java.io.File"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.LecturesBean"%>
<%@page import="dao.SubjectsDAO"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% LecturesBean lBean = (LecturesBean) request.getAttribute("lecturesBean"); %>
<% String body = lBean.getBody(); %>
<% String fileName = lBean.getFileName(); %>
<% String fileExt = (fileName.lastIndexOf(".") > 0) ? fileName.substring(fileName.lastIndexOf(".") + 1) : "_blank"; %>
<% Map<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap(); %>

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
	<h1 class="lead"><%= subjectsMap.get(lBean.getSubjectId()) %></h1>
	<h2><%= lBean.getTitle() %></h2>
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
