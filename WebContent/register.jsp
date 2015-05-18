<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Register form</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">

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
				<li><a href="<%= request.getContextPath() %>">Home</a></li>
				<li class="active"><a href="<%= request.getContextPath() %>/register">Register</a></li>
				<li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
			</ul>
		</div>
	</div>
	</nav>
	<div class="container">
		<% String status = (String) request.getAttribute("status"); %>
		<%	String message = (String) request.getAttribute("message"); %>
	
		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		<div class="page-header">
		  <h1>User registration</h1>
		</div>

	<form action="register" id="form" method="post" class="form-horizontal">	
	<div class="form-group">
	    <label for="username" class="col-sm-2 control-label required">Username*</label>
	    <div class="col-sm-10">
	      <input name="username" type="text" class="form-control" id="username" placeholder="Username*" required>
	    </div>
	  </div>
	  
	  <div class="form-group">
    <label for="pass" class="col-sm-2 control-label required">Password*</label>
    <div class="col-sm-10">
      <input name="pass" type="password" class="form-control" id="pass" placeholder="Password*" required>
    </div>
  </div>
  
  <div class="form-group">
	    <label for="username" class="col-sm-2 control-label required">Email*</label>
	    <div class="col-sm-10">
	      <input name="email" type="email" class="form-control" placeholder="Email*" required>
	    </div>
	  </div>
	  
	  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <div class="radiobutton">
        <label>
          	<input type="radio" name="role" value="0" checked>Student
        </label>
      </div>
    </div>
    <div class="col-sm-offset-2 col-sm-10">
      <div class="radiobutton">
        <label>
	        <input type="radio" name="role" value="1">Teacher
        </label>
      </div>
    </div>
    <div class="col-sm-offset-2 col-sm-10">
      <div class="radiobutton">
        <label>
	        <input type="radio" name="role" value="2">Admin
        </label>
      </div>
    </div>
  </div>
  
  <div class="form-group">
	    <label for="firstname" class="col-sm-2 control-label required">First Name</label>
	    <div class="col-sm-10">
	      <input name="firstname" type="text" class="form-control" id="firstname" placeholder="First Name" required>
	    </div>
	  </div>
	  
	  <div class="form-group">
	    <label for="lastname" class="col-sm-2 control-label required">Last Name</label>
	    <div class="col-sm-10">
	      <input name="lastname" type="text" class="form-control" id="lastname" placeholder="Last Name" required>
	    </div>
	  </div>
	  
	  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-primary">Create user</button>
    </div>
  </div>
	</form>
	</div>
</body>
</html>