<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<% String basePath = request.getContextPath(); %>
<% Byte userRole = (Byte) request.getAttribute("userRole"); %>
<% String status = (String) request.getAttribute("status"); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Main page</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link rel="icon" href="favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

</head>
<body>
	
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="<%= request.getContextPath() %>">Students Test Project</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="<%= basePath %>">Home</a></li>
					
					<% if (userRole.byteValue() == 2) { %>
						<li><a href="<%= basePath %>/users">Users</a></li>
						<li><a href="<%= basePath %>/register">Register</a></li>
					<% } %>
				</ul>
					<ul class="nav navbar-nav navbar-right">
						<li><p class="navbar-text welcome">Welcome</p></li>
						<li class="dropdown">
    						<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-expanded="false">
      							${currentUser} <span class="caret"></span>
    						</a>
    						<ul class="dropdown-menu" role="menu">
    							<li><a href="<%= basePath %>/user/<%= user.getId() %>">Edit profile</a></li>
      							<li><a href="<%= basePath %>/logout">Logout</a></li>
    						</ul>
  						</li>
  					</ul>
				</div>
			</div>
		</nav>

	<div class="container">
			
			<% if (userRole.byteValue() == 0) { %>
				<div class="main-page">
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/lectures" class="thumbnail">
	      					<img src="imgs/lectures.jpg" alt="Lections">
	      					<h2><small>Lectures</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/practicals" class="thumbnail">
	      					<img src="imgs/practicals.jpg" alt="Practical">
	      					<h2><small>Practicals</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/tests" class="thumbnail">
	      					<img src="imgs/tests.jpg" alt="Tests">
	      					<h2><small>Tests</small></h2>
	    				</a>
	  				</div>
  				</div>
			<% } else { %>
				<div class="main-page">
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/lectures" class="thumbnail">
	      					<img src="imgs/lectures.jpg" alt="Lections">
	      					<h2><small>Lectures</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/practicals" class="thumbnail">
	      					<img src="imgs/practicals.jpg" alt="Practical">
	      					<h2><small>Practicals</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/tests" class="thumbnail">
	      					<img src="imgs/tests.jpg" alt="Tests">
	      					<h2><small>Tests</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
   						<a href="<%= basePath %>/students" class="thumbnail">
      						<img src="imgs/students.jpg" alt="Students">
      						<h2><small>Students</small></h2>
    					</a>
  					</div>
  				</div>	
			<% } %>			
	</div>
</body>
</html>
