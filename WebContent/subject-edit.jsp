<%@page import="java.io.File"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.SubjectsBean"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% SubjectsBean sBean = (SubjectsBean) request.getAttribute("subjectsBean"); %>
<% HashMap<Integer, String> departmentsMap = (HashMap<Integer, String>) request.getAttribute("departmentsMap"); %>
<% String subjectName = sBean.getSubjectName(); %>
<% String department = departmentsMap.get(sBean.getDepartmentId()); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
	<div class="alert alert-${status}">
		<p>${message}</p>
	</div>
	<% } %>
	<h3 class="lead">Edit subject</h3>
	
		<div class="form-group">
			<label for="subject" class="col-sm-2 control-label">Subject Name*</label>
			<div class="col-sm-10">
				<input name="subjectName" type="text" class="form-control typeahead"
					class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/subjects" value="<%= subjectName %>">
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Department*</label>
			<div class="col-sm-10">
				<input name="departmentName" type="text" class="form-control" id="title" required value="<%= department %>">
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-primary update-subject" data-id="<%= sBean.getId() %>">Update</button>
			</div>
		</div>
</div>

<%@ include file="footer.jsp" %>
