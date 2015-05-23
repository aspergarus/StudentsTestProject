<%@page import="beans.LectureBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    
<% String basePath = request.getContextPath(); %>
<% ArrayList<LectureBean> lectures = (ArrayList<LectureBean>) request.getAttribute("lecturesList"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>My lectures</title>

<meta http-equiv="Content-Type" charset=utf-8 content="text/html">

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

		<div class="starter-template">
			<h1>Lectures</h1>
			<br>
			
			<form class="navbar-form navbar-left" method="post" enctype="multipart/form-data" action="upload">
    			<input type="text" class="form-control add-file" placeholder="Title">
 				<input type="file" class="btn btn-default add-file" name="file">
 				<button type="submit" class="btn btn-default">Add lection</button>
			</form>
			
			<table id="table" data-search="true" data-show-columns="true">
				<% out.print(lectures == null ? "Lections are not exists" : ""); %>
			    <thead>
			        <tr>
			            <th data-field="id" data-align="center" data-sortable="true">Number</th>
			            <th data-field="title" data-align="center" data-sortable="true">Title</th>
			            <th data-field="firstName" data-align="center" data-sortable="true">Size</th>
			            <th data-field="edit" data-align="center">Edit</th>
			        </tr>
			    </thead>
			    <tbody>
			    <% for (LectureBean lecture: lectures) { %>
					<tr>
				        <td><% out.print(lecture.getId()); %></td>
				        <td><% out.print(lecture.getTitle()); %></td>
				        <td><% out.print(lecture.getSize()); %></td>
				        <td><a href="" target="_blank">Edit</a></td>
				    </tr>
				<% } %>
				</tbody>
			</table>

		</div>
	</div>
	
</body>
</html>