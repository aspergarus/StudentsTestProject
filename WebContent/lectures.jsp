<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="beans.LecturesBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% Byte userRole = (Byte) request.getAttribute("userRole"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% Map<String, ArrayList<LecturesBean>> lecturesMap = (HashMap<String, ArrayList<LecturesBean>>) request.getAttribute("lecturesMap"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Lectures</title>

<meta http-equiv="Content-Type" charset=utf-8 content="text/html">

<link rel="shortcut icon" href="<%= basePath %>/favicon.ico" type="image/x-icon">
<link rel="icon" href="<%= basePath %>/favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="<%= basePath %>/css/style.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap-theme.css">
<link rel="stylesheet" href="<%= basePath %>/css/bootstrap-table.min.css">

<!-- js libraries -->
<script src="<%= basePath %>/js/jquery.min.js"></script>
<script src="<%= basePath %>/js/bootstrap.min.js"></script>
<script src="<%= basePath %>/js/bootstrap-table.min.js"></script>
<script src="<%= basePath %>/js/bootstrap-typeahead.min.js"></script>

<!-- Custom javascripts -->
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
				<li><a href="<%= basePath %>/users">Users</a></li>
				<li><a href="<%= basePath %>/register">Register</a></li>
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
		
	<% if (userRole != 0) { %>
		
		<h3 class="lead">Add lecture</h3>
		<form action="<%= basePath %>/lectures" class="form" method="post"
			class="form-horizontal" enctype="multipart/form-data">
			<div class="form-group">
				<label for="subject" class="col-sm-2 control-label">Subject*</label>
				<div class="col-sm-10">
					<input name="subject" type="text" class="form-control typeahead"
						class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/lecturesSubjects">
				</div>
			</div>
 
			<div class="form-group">
				<label for="title" class="col-sm-2 control-label">Title*</label>
				<div class="col-sm-10">
					<input name="title" type="text" class="form-control" id="title" required>
				</div>
			</div>

			<div class="form-group">
				<label for="body" class="col-sm-2 control-label required">Body</label>
				<div class="col-sm-10">
					<textarea name="body" class="form-control" rows="3"></textarea>
				</div>
			</div>

			<div class="form-group">
				<label for="upload" class="col-sm-2 control-label required">Upload files</label>
				<div class="col-sm-10">
					<input type="file" name="upload" id="upload" class="form-control" accept="application/msword, application/pdf">
					<p class="help-block">File size not more then 10 MB. Allowed formats: pdf, doc, docx.</p>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Add lecture</button>
				</div>
			</div>
		</form>
	
	<% } %>
	</div>
	
	
	
	<div class="container">
		<h1>Lectures</h1>
		<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			<% int i = 0; %>
			<% for (String subject : lecturesMap.keySet()) { %>
			<% i++; %>
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="heading-<%= i %>">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-parent="#accordion" href="#collapse-<%= i %>" aria-expanded="true" aria-controls="collapse-<%= i %>">
						  <%= subject %>
						</a>
					</h4>
				</div>
				<div id="collapse-<%= i %>" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading-<%= i %>">
					<div class="panel-body">
						<table class="table">
							<thead>
						        <tr>
						            <th data-field="title" data-align="center" data-sortable="true">Title</th>
						            <th data-field="view" data-align="center">View</th>
						            <% if (userRole != 0) { %>
						            <th data-field="edit" data-align="center">Edit</th>
						            <th data-field="delete" data-align="center">Delete</th>
						            <% } %>
						        </tr>
						    </thead>
						    <tbody>
						    <% for (LecturesBean lecture : lecturesMap.get(subject)) { %>
						    	<tr>
									<td><%= lecture.getTitle() %></td>
							        <td><a href="practicals/<%= lecture.getId() %>">View</a></td>
							        <% if (userRole != 0) { %>
							        <td><a href="practicals/<%= lecture.getId() %>/edit">Edit</a></td>
							        <td><a href="practicals/<%= lecture.getId() %>/delete">Delete</a></td>
							        <% } %>
						        </tr>
							<% } %>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<% } %>
		</div>
	</div>

		
	
</body>
</html>