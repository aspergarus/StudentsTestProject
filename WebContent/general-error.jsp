<%@page import="beans.UserBean"%>
<%@page isErrorPage="true" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean userBean = (UserBean) session.getAttribute("user"); %>
<% String exceptionMessage = exception != null ? exception.getMessage() : ""; %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error page</title>

<link rel="shortcut icon" href="<%= basePath %>/favicon.ico" type="image/x-icon">
<link rel="icon" href="<%= basePath %>/favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="<%= basePath %>/css/style.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap-theme.min.css">

<script src="<%= basePath %>/js/jquery.min.js"></script>
<script src="<%= basePath %>/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="<%= request.getContextPath() %>">Students Test Project</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="<%= basePath %>">Home</a></li>
					<% if (userBean == null) { %>
						<li><a href="<%= basePath %>/login">Login</a></li>
					<% } else { %>
						<li><a href="<%= basePath %>/logout">Logout</a></li>
					<% } %>
				</ul>
			</div>
		</div>
	</nav>

<div class="error404">
	<% if (exceptionMessage.isEmpty()) { %>
		<h1>404</h1><br>
		<h2><small>File not found</small></h2><br><br>
		<h3><small>The site configured at this address not contain the requested file.</small></h3><br>
		<h3><small>If this is your site, make sure that the filename case matches the URL.</small></h3>
	<% } else { %>
		<h1>Exception</h1><br>
		<p class="lead"><%= exceptionMessage %></p>
	<% } %>
</div>
</body>
</html>