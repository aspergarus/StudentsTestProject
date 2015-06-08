<%@page import="java.io.File"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.PracticalsBean"%>
<%@page import="dao.SubjectsDAO"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% saveDir = saveDir.replaceAll("\\\\", "/");; %>
<% PracticalsBean pBean = (PracticalsBean) request.getAttribute("practicalBean"); %>
<% String body = pBean.getBody(); %>
<% String fileName = pBean.getFileName(); %>
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
	<h1 class="lead"><%= subjectsMap.get(pBean.getSubjectId()) %></h1>
	<h2><%= pBean.getTitle() %></h2>
	<% if (!body.isEmpty()) { %>
		<div class="body"><%= body %></div>
	<% } %>
	<% if (!fileName.isEmpty()) { %>
		<div class="file">
			<a href="<%= saveDir + "/" + fileName %>">
			<img src="imgs/icons/<%= fileExt %>.png" alt="<%= fileName %>" />
			<%= fileName %>
			</a>
		</div>
	<% } %>
</div>

<%@ include file="footer.jsp" %>
