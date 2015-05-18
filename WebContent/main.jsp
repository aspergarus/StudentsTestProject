<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Main page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</head>
<body>
	<% Byte userRole = (Byte) request.getAttribute("userRole"); %>
	<% String status = (String) request.getAttribute("status"); %>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<%= request.getContextPath() %>">Students Test Project</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="<%= request.getContextPath() %>">Home</a></li>
				<% if (userRole.byteValue() == 2) { %>
					<li><a href="<%= request.getContextPath() %>/register">Register</a></li>
				<% } %>
				<li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
			</ul>
		</div>
	</div>
	</nav>


	<div class="container">

		<div class="starter-template">
			<h1>Welcome, ${currentUser}</h1>
			<p class="lead">Currently site is under construction. Please,
				wait for content.</p>
		</div>

	</div>


</body>
</html>