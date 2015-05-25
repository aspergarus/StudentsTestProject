<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% ArrayList<UserBean> students = (ArrayList<UserBean>) request.getAttribute("studentList"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>

<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>

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

<%@ include file="footer.jsp" %>
