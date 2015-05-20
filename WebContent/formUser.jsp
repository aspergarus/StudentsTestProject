<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% UserBean editedUser = (UserBean) request.getAttribute("editedUser"); %>
<% byte userRole = (byte) request.getAttribute("userRole"); %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Edit user</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" href="<%= basePath %>/favicon.ico" type="image/x-icon">
<link rel="icon" href="<%= basePath %>/favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="<%= basePath %>/css/style.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap-theme.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap-table.min.css">

<script src="<%= basePath %>/js/jquery.min.js"></script>
<script src="<%= basePath %>/js/bootstrap.min.js"></script>
<script src="<%= basePath %>/js/bootstrap-table.min.js"></script>
<script src="<%= basePath %>/js/script.js"></script>

</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<%= basePath %>">Students
				Test Project</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="<%= basePath %>">Home</a></li>
				<% if (userRole == 2) { %>
					<li class="active"><a href="<%= basePath %>/users">Users</a></li>
					<li><a href="<%= basePath %>/register">Register</a></li>
				<% } %>
				<li><a href="<%= basePath %>/logout">Logout</a></li>
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
		<div class="page-header">
			<h1>User registration</h1>
		</div>

		<form action="<%= basePath %>/user/<%= editedUser.getId() %>" id="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="editedusername" class="col-sm-2 control-label required">Username*</label>
				<div class="col-sm-10">
					<input name="editedusername" type="text" class="form-control"
						id="editedusername" placeholder="Username*" required value="<%= editedUser.getUsername() %>">
				</div>
			</div>
 
			<div class="form-group">
				<label for="editedpass" class="col-sm-2 control-label required">Password*</label>
				<div class="col-sm-10">
					<input name="editedpass" type="password" class="form-control" id="editedpass"
						placeholder="You can leave the old password. Just don't fill this field">
				</div>
			</div>

			<div class="form-group">
				<label for="editedemail" class="col-sm-2 control-label required">Email*</label>
				<div class="col-sm-10">
					<input name="editedemail" type="email" class="form-control"
						placeholder="Email*" required value=<%= editedUser.getEmail() %>>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<div class="radiobutton">
						<label> <input type="radio" name="role" value="0" <%= editedUser.getRole() == 0 ? "checked" : "" %>>Student
						</label>
					</div>
				</div>
				<div class="col-sm-offset-2 col-sm-10">
					<div class="radiobutton">
						<label> <input type="radio" name="role" value="1" <%= editedUser.getRole() == 1 ? "checked" : "" %>>Teacher
						</label>
					</div>
				</div>
				<div class="col-sm-offset-2 col-sm-10">
					<div class="radiobutton">
						<label> <input type="radio" name="role" value="2" <%= editedUser.getRole() == 2 ? "checked" : "" %>>Admin
						</label>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="firstname" class="col-sm-2 control-label required">First
					Name</label>
				<div class="col-sm-10">
					<input name="firstname" type="text" class="form-control"
						id="firstname" placeholder="First Name" required value="<%= editedUser.getFirstName() == null ? "" : editedUser.getFirstName() %>">
				</div>
			</div>

			<div class="form-group">
				<label for="lastname" class="col-sm-2 control-label required">Last
					Name</label>
				<div class="col-sm-10">
					<input name="lastname" type="text" class="form-control"
						id="lastname" placeholder="Last Name" required value="<%= editedUser.getLastName() == null ? "" : editedUser.getLastName() %>">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Save changes</button>
				</div>
			</div>
			<input type="hidden" name="id" value="<%= editedUser.getId() %>">
		</form>
	</div>

</body>
</html>
