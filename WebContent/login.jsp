<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Login page</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="icon" href="favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<% String basePath = request.getContextPath(); %>
<% String error = (String) request.getAttribute("error"); %>

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


	<div class="container">
		<% if (error != null) { %>
			<div class="error">
				<p>${error}</p>
			</div>
		<% } %>

		<form class="form-signin" action="login" method="post">
			<h2 class="form-signin-heading">Please sign in</h2>
			<label for="login" class="sr-only">Email address</label> <input
				type="text" id="login" class="form-control"
				placeholder="Email address" required="" autofocus="" name="login">
			<label for="pass" class="sr-only">Password</label> <input
				type="password" id="pass" class="form-control"
				placeholder="Password" required="" name="pass">
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
		</form>

	</div>
</body>
</html>