<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Students Project</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="icon" href="favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">

<script>var basePath = "<%= basePath %>";</script>

<script src="js/lib/jquery.min.js"></script>
<script src="js/lib/bootstrap/bootstrap.min.js"></script>
<script src="js/lib/bootstrap/bootstrap-table.js"></script>

<script src="js/translate.js"></script>
<script src="js/script.js"></script>

</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="<%= request.getContextPath() %>">
					<span class="translate" data-lang-key="Students Project"></span>
				</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="<%= basePath %>"><span class="translate" data-lang-key="Home"></span></a></li>
					<li class="active"><a
						href="<%= basePath %>/login"><span class="translate" data-lang-key="Login"></span></a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">
		<%	if (status != null && message != null) { %>
			<div class="alert alert-${status}">
				<p>${message}</p>
			</div>
		<% } %>

		<form class="form-signin" action="login" method="post">
			<h2 class="form-signin-heading">
				<span class="translate" data-lang-key="Please sign in"></span>
			</h2>
			<label for="login" class="sr-only"><span class="translate" data-lang-key="Email address or Username"></span></label>
			<input type="text" id="login" class="form-control" 
				placeholder="Email address or Username" name="login" required autofocus>
			
			<label for="pass" class="sr-only"><span class="translate" data-lang-key="Password"></span></label>
			<input type="password" id="pass" class="form-control"
				placeholder="Password" name="pass" required>
			<button class="btn btn-lg btn-primary btn-block" type="submit">
				<span class="translate" data-lang-key="Sign In"></span>
			</button>
		</form>
	</div>
</body>
</html>
