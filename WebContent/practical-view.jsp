<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.PracticalsBean"%>
<%@page import="beans.FileBean"%>
<%@page import="dao.SubjectsDAO"%>
<%@page import="dao.FileDAO"%>
<%@page import="dao.FileDAO"%>
<%@page import="util.FileUploadManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% saveDir = saveDir.replaceAll("\\\\", "/");; %>
<% PracticalsBean pBean = (PracticalsBean) request.getAttribute("practicalBean"); %>
<% String body = pBean.getBody(); %>
<% ArrayList<FileBean> fileBeans = FileDAO.findAll(pBean.getId(), "practicals"); %>
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
