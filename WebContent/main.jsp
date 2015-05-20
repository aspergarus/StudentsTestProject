<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<% String basePath = request.getContextPath(); %>
<% Byte userRole = (Byte) request.getAttribute("userRole"); %>
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
					<li class="active"><a href="<%= basePath %>">Home</a></li>
					<% if (userRole.byteValue() == 2) { %>
						<li><a href="<%= basePath %>/users">Users</a></li>
						<li><a href="<%= basePath %>/register">Register</a></li>
					<% } %>
					<li><a href="<%= basePath %>/logout">Logout</a></li>
				</ul>
			</div>
		</div>
	</nav>


	<div class="container">

		
			<h1>Welcome, ${currentUser}</h1>
			
			<% if (userRole.byteValue() == 0) { %>
				<div class="main-page">
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/lectures" class="thumbnail">
	      					<img src="lectures.jpg" alt="Lections">
	      					<h2><small>Lectures</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/practicals" class="thumbnail">
	      					<img src="practicals.jpg" alt="Practical">
	      					<h2><small>Practicals</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/tests" class="thumbnail">
	      					<img src="tests.jpg" alt="Tests">
	      					<h2><small>Tests</small></h2>
	    				</a>
	  				</div>
  				</div>
					<% } else %>
					
			<% if (userRole.byteValue() == 1) { %>
				<div class="main-page">
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/lectures" class="thumbnail">
	      					<img src="lectures.jpg" alt="Lections">
	      					<h2><small>Lectures</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/practicals" class="thumbnail">
	      					<img src="practicals.jpg" alt="Practical">
	      					<h2><small>Practicals</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
	   					<a href="<%= basePath %>/tests" class="thumbnail">
	      					<img src="tests.jpg" alt="Tests">
	      					<h2><small>Tests</small></h2>
	    				</a>
	  				</div>
	  				
	  				<div class="col-xs-6 col-md-3">
   						<a href="<%= basePath %>/students" class="thumbnail">
      						<img src="students.jpg" alt="Students">
      						<h2><small>Students</small></h2>
    					</a>
  					</div>
					<% } %>
			

		

	</div>


</body>
</html>
