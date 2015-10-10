<%@page import="beans.SubjectsBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<SubjectsBean> subjects = (ArrayList<SubjectsBean>) request.getAttribute("subjectsList"); %>
<% HashMap<Integer, String> departmentsMap = (HashMap<Integer, String>) request.getAttribute("departmentsMap"); %>

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
					<input name="subjectName" type="text" class="form-control subjectName"
						required autocomplete="off">
				</div>
			</div>
			<div class="form-group">
				<label for="departmentName" class="col-sm-2 control-label">Department*</label>
				<div class="col-sm-10">
					<input name="departmentName" type="text" class="form-control typeahead" id="departmentName" required
						autocomplete="off" data-autocomplete-url="autocomplete/departments">
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
	<% if (subjects.size() == 0) { %>
		<h1>There are no subjects yet</h1>
	<% } else { %>
		<h1>Subjects (<%= subjects.size() %>)</h1>
	<% } %>
	<table class="table" data-search="true" data-show-columns="true">
		<% out.print(subjects == null ? "Subjects are not exists" : ""); %>
		<thead>
			<tr>
				<% if (user.getRole() == 2) { %>
				<th data-field="id" data-align="center" data-sortable="true">ID</th>
				<% } %>
				<th data-field="subjectName" data-align="center" data-sortable="true">Subject Name</th>
				<th data-field="department" data-align="center" data-sortable="true">Department</th>
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
			        <td><% out.print (departmentsMap.get(subject.getDepartmentId())); %></td>
			        <% if (user.getRole() == 2) { %>
			        <td><a href="subjects?edit=true&id=<%= subject.getId() %>">Edit</a></td>
			        <td>
						<button class="btn btn-danger" data-id="<%= subject.getId() %>"
							data-path="/subjects">Delete</button>
			        </td>
			        <% } %>
			    </tr>
			<% } %>
		</tbody>
	</table>
</div>

<%@ include file="footer.jsp" %>
