<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error access page</title>

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="icon" href="favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>

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
					<li class="active"><a
						href="<%= basePath %>/login">Login</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<% 
		String message = (String) request.getAttribute("message");
		if (message != null) {
	%>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	
	
	
	
</body>
</html>