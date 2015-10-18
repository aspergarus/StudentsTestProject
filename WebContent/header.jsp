<%@page import="beans.UserBean"%>
<% String basePathHeader = request.getContextPath(); %>
<% UserBean userBean = (UserBean) session.getAttribute("user"); %>
<% int uid = userBean != null ? userBean.getId() : 0; %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Student test project</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" href="<%= basePathHeader %>/favicon.ico" type="image/x-icon">
<link rel="icon" href="<%= basePathHeader %>/favicon.ico" type="image/x-icon">

<link rel="stylesheet" href="<%= basePathHeader %>/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/bootstrap-theme.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/bootstrap-table.min.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/fileinput.min.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/bootstrap-tokenfield.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/jquery-ui.min.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/style.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/bootstrap-dialog.min.css">
<link rel="stylesheet" href="<%= basePathHeader %>/css/TimeCircles.css"> 
<link rel="stylesheet" href="<%= basePathHeader %>/css/sweetalert.css"> 

<!-- js libraries -->
<script  src="<%= basePathHeader %>/js/jquery.min.js"></script>
<script src="<%= basePathHeader %>/js/jquery-ui.min.js"></script>
<script src="<%= basePathHeader %>/js/bootstrap.min.js"></script>
<script src="<%= basePathHeader %>/js/bootstrap-table.min.js"></script>
<script src="<%= basePathHeader %>/js/bootstrap-typeahead.js"></script>
<script src="<%= basePathHeader %>/js/fileinput.js"></script>
<script src="<%= basePathHeader %>/js/fileinput_locale_uk.js"></script>
<script src="<%= basePathHeader %>/js/bootstrap-tokenfield.js"></script>
<script src="<%= basePathHeader %>/js/bootstrap-dialog.min.js"></script>
<script src="<%= basePathHeader %>/js/lib/ck/ckeditor.js"></script>
<script src="<%= basePathHeader %>/js/TimeCircles.js"></script>
<script src="<%= basePathHeader %>/js/sweetalert.min.js"></script>

<!-- Custom javascripts variables -->
<script>var basePath = "<%= basePathHeader %>";</script>
<script>var userId = "<%= uid %>";</script>
<!-- Custom javascripts -->
<script src="<%= basePathHeader %>/js/script.js"></script>

</head>
<body>
