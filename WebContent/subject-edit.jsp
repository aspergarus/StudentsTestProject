<%@page import="java.io.File"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.SubjectsBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% SubjectsBean sBean = (SubjectsBean) request.getAttribute("subjectsBean"); %>
<% String subjectName = sBean.getSubjectName(); %>
<% String department = sBean.getDepartment(); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
	<div class="alert alert-${status}">
		<p>${message}</p>
	</div>
	<% } %>
	<h3 class="lead">Edit subject</h3>
	<form action="<%= basePath %>/subjects" class="form" method="post"
		class="form-horizontal">
		<div class="form-group">
			<label for="subject" class="col-sm-2 control-label">Subject Name*</label>
			<div class="col-sm-10">
				<input name="subjectName" type="text" class="form-control typeahead"
					class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/subjects" value="<%= sBean.getSubjectName() %>">
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Department*</label>
			<div class="col-sm-10">
				<input name="department" type="text" class="form-control" id="title" required value="<%= sBean.getDepartment() %>">
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button type="submit" class="btn btn-primary">Update</button>
			</div>
		</div>
		<input type="hidden" name="update-id" value="<%= sBean.getId() %>">
	</form>
</div>

<%@ include file="footer.jsp" %>
