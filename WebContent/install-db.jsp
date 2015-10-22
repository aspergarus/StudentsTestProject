<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<% String basePath = request.getContextPath(); %>
<% int statusDB = (int) request.getAttribute("statusDB"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Install page</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="icon" href="favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/createTables.js"></script>

<script>var basePath = "<%= basePath %>";</script>

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
		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		
		<%	if (statusDB == -1) { %>
		
		<h3 class="lead">DataBase Settings</h3>
		
		<form action="<%= basePath %>/install" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="host" class="col-sm-2 control-label">Host*</label>
				<div class="col-sm-10">
					<input name="host" type="text" class="form-control typeahead"
						class="host">
				</div>
				<label for="port" class="col-sm-2 control-label">Port*</label>
				<div class="col-sm-10">
					<input name="port" type="text" class="form-control typeahead"
						class="port">
				</div>
				<label for="dataBaseName" class="col-sm-2 control-label">DataBase Name*</label>
				<div class="col-sm-10">
					<input name="dataBaseName" type="text" class="form-control typeahead"
						class="dataBaseName">
				</div>
				<label for="admin" class="col-sm-2 control-label">Admin*</label>
				<div class="col-sm-10">
					<input name="admin" type="text" class="form-control typeahead"
						class="admin">
				</div>
				<label for="password" class="col-sm-2 control-label">Password*</label>
				<div class="col-sm-10">
					<input name="password" type="password" class="form-control typeahead"
						class="password">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 add-subject">
					<button type="submit" class="btn btn-primary">Apply Settings</button>
				</div>
			</div>
		</form>
		
		<% } else { %>
		
			<h3 class="lead">You don't have <%= statusDB %> tables in your DB. Do you want to create them?</h3>
			<h4 class="lead"><small>We can do it for you.</small></h4>
			<br>
			<h5 class="lead"><small>If you don't have account fill the form below.</small></h5>
			<label for="name" class="col-sm-2 control-label">Username*</label>
			<div class="col-sm-10">
				<input name="name" id="name" type="text" class="form-control typeahead"
					class="name">
			</div>
			<label for="pass" class="col-sm-2 control-label">Password*</label>
			<div class="col-sm-10">
				<input name="pass" id="pass" type="password" class="form-control typeahead"
					class="pass">
			</div>
			<hr>
			<div class="col-sm-offset-2 col-sm-10 add-subject">
				<button type="submit" class="btn btn-primary create-tables">Create tables</button>
			</div>
		<% } %>
		
	</div>
</body>
</html>
