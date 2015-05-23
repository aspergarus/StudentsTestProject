<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% ArrayList<UserBean> students = (ArrayList<UserBean>) request.getAttribute("studentList"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Users</title>

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
<script src="<%= basePath %>/js/bootstrap-typeahead.min.js"></script>

<script src="<%= basePath %>/js/script.js"></script>

</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<%= basePath %>">Students Test Project</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="<%= basePath %>">Home</a></li>
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
		<h3 class="lead">Add practical</h3>
		<form action="<%= basePath %>/students" id="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="studentname" class="col-sm-2 control-label">Student name</label>
				<div class="col-sm-10">
					<input name="name" type="text" class="form-control typeahead"
						id="name" required autocomplete="off" data-autocomplete-url="autocomplete/students">
					<p class="help-block">You can find students by first or last name.</p>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Add to the list</button>
				</div>
			</div>
		</form>
	</div>

	<div class="container">

		<div class="starter-template">
			<h1>Students</h1>
			<table class="table" data-search="true">
				<% out.print(students == null ? "You don't have any students" : ""); %>
			    <thead>
			        <tr>
			            <th data-field="firstName" data-align="center" data-sortable="true">First Name</th>
			            <th data-field="lastName" data-align="center" data-sortable="true">Last Name</th>
			            <th data-field="delete" data-align="center">Delete from list</th>
			        </tr>
			    </thead>
			    <tbody>
			    <% for (UserBean student: students) { %>
					<tr>
				        <td><% out.print(student.getFirstName()); %></td>
				        <td><% out.print(student.getLastName()); %></td>
				        <td>
				        	<form action="<%= basePath %>/students" method="post">
					        	<button type="submit" class="btn btn-danger">Delete</button>
					        	<input type="hidden" name="delete-id" value="<%= student.getId() %>">
				        	</form>
				        </td>
				    </tr>
				<% } %>
				</tbody>
			</table>

		</div>
	</div>

</body>
</html>
