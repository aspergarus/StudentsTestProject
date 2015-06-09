<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.FileBean"%>
<%@page import="beans.LecturesBean"%>
<%@page import="dao.SubjectsDAO"%>
<%@page import="dao.FileDAO"%>
<%@page import="util.FileUploadManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% LecturesBean lBean = (LecturesBean) request.getAttribute("lecturesBean"); %>
<% String body = lBean.getBody(); %>
<% ArrayList<FileBean> fileBeans = FileDAO.findAll(lBean.getId(), "lectures"); %>
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
	<% for (FileBean fileBean : fileBeans) { %>
		<div class="file">
			<a href="<%= saveDir + File.separator + fileBean.getName() %>" download>
			<img src="imgs/icons/<%= FileUploadManager.extractFileExt(fileBean.getName()) %>.png" alt="<%= fileBean.getName() %>" />
			<%= fileBean.getName() %>
			</a>
		</div>
	<% } %>
</div>

<%@ include file="footer.jsp" %>
