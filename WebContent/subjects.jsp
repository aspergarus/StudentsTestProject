<%@page import="beans.SubjectsBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<SubjectsBean> subjects = (ArrayList<SubjectsBean>) request.getAttribute("subjectsList"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	
	<% if (user.getRole() == 2) { %>
		
		<h3 class="lead">Add subject</h3>

		<form action="<%= basePath %>/subjects" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="subjectName" class="col-sm-2 control-label">Subject name*</label>
				<div class="col-sm-10">
					<input name="subjectName" type="text" class="form-control typeahead"
						class="subjectName" required autocomplete="off" data-autocomplete-url="autocomplete/subjects">
				</div>
			</div>
			<div class="form-group">
				<label for="department" class="col-sm-2 control-label">Department*</label>
				<div class="col-sm-10">
					<input name="department" type="text" class="form-control" id="department" required>
				</div>
			</div>	
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 add-subject">
					<button type="submit" class="btn btn-primary">Add subject</button>
				</div>
			</div>
		</form>
	<% } %>
</div>
	
<div class="container">
	<h1>Subjects</h1>
	<table class="table" data-search="true" data-show-columns="true">
		<% out.print(subjects == null ? "Subjects are not exists" : ""); %>
		<thead>
			<tr>
				<% if (user.getRole() == 2) { %>
				<th data-field="id" data-align="center" data-sortable="true">ID</th>
				<% } %>
				<th data-field="subjectName" data-align="center" data-sortable="true">Subject Name</th>
				<th data-field="department" data-align="center" data-sortable="true">Department</th>
				<th data-field="teacher" data-align="center" data-sortable="true">Teacher</th>
				<% if (user.getRole() == 2) { %>
				<th data-field="edit" data-align="center">Edit</th>
				<th data-field="delete" data-align="center">Delete</th>
				<% } %>
			</tr>
		</thead>
		<tbody>
		    <% for (SubjectsBean subject: subjects) { %>
				<tr>
					<% if (user.getRole() == 2) { %>
			        <td><% out.print(subject.getId()); %></td>
			        <% } %>
			        <td><% out.print(subject.getSubjectName()); %></td>
			        <td><% out.print(subject.getDepartment()); %></td>
			        <td><% out.print(subject.getTeacherId() == 0 ? "Викладач не призначений" : subject.getTeacherId()); %></td>	
			        <% if (user.getRole() == 2) { %>
			        <td><a href="subjects?edit=true&id=<%= subject.getId() %>">Edit</a></td>
			        <td>
			        	<form action="<%= basePath %>/subjects" method="post">
							<button type="submit" class="btn btn-danger">Delete</button>
							<input type="hidden" name="delete-id" value="<%= subject.getId() %>">
						</form>
			        </td>
			        <% } %>
			    </tr>
			<% } %>
		</tbody>
	</table>
</div>

<%@ include file="footer.jsp" %>
