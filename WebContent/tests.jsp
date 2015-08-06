<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="beans.TestBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean currentUser = (UserBean) request.getAttribute("currentUser"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% HashMap<String, ArrayList<TestBean>> tests = (HashMap<String, ArrayList<TestBean>>) request.getAttribute("testsMap"); %>
<% HashMap<Integer, String> subjects = (HashMap<Integer, String>) request.getAttribute("subjects");  %>
<% HashMap<Integer, String> teachers = (HashMap<Integer, String>) request.getAttribute("teachers"); %>
<% HashMap<String, String> groups = (HashMap<String, String>) request.getAttribute("groupsMap"); %>

<%@ include file="header.jsp"%>

<%@ include file="menu.jsp"%>
	
<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
		
	<% if (currentUser.getRole() > 0) { %>
		
		<h3 class="lead">Add a Test</h3>

		<form action="<%= basePath %>/tests" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="subject" class="col-sm-2 control-label">Subject*</label>
				<div class="col-sm-10">
					<input name="subject" type="text" class="form-control typeahead"
						required autocomplete="off" data-autocomplete-url="autocomplete/subjects">
				</div>
			</div>
			
			<% if (currentUser.getRole() == 2) { %>
				<div class="form-group">
					<label for="teacher" class="col-sm-2 control-label">Teacher*</label>
					<div class="col-sm-10">
						<input name="teacher" type="text" class="form-control typeahead"
							required autocomplete="off" data-autocomplete-url="autocomplete/teachers">
					</div>
				</div>
			<% } %>
			
			<div class="form-group">
				<label for="module" class="col-sm-2 control-label">Module*</label>
				<div class="col-sm-10">
					<input name="module" type="number" class="form-control" required>
				</div>
			</div>
			
			<div class="form-group">
				<label for="note" class="col-sm-2 control-label required">Note</label>
				<div class="col-sm-10">
					<textarea name="note" class="form-control text-area" rows="3"></textarea>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Add a test</button>
				</div>
			</div>
		</form>
	<% } %>
</div>

<div class="container">
	<h1>Tests</h1>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
		<% int i = 0; %>
		<% for (String subject : tests.keySet()) { %>
		<% i++; %>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="heading-<%= i %>">
				<h4 class="panel-title">
					<a class="subject-<%= i %>" data-toggle="collapse" data-parent="#accordion" href="#collapse-<%= i %>" aria-controls="collapse-<%= i %>">
					  <%= subject %>
					</a>
				</h4>
			</div>

			<div id="collapse-<%= i %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-<%= i %>">
				<div class="panel-body">
				
				<% if (currentUser.getRole() > 0) { %>
					<p class="help-block">Share this subject to groups:
						<input type="text" class="tokenfield" value="<%= groups.get(subject) == null ? "" :  groups.get(subject) %>" name="groupName" required autocomplete="off" />
						<input type="submit" value="Share" class="btn btn-info btn-share assign-subject-group" data-num="<%= i %>" data-subject="<%= subject %>">
					</p>
				<% } %>
					
					<table class="table">
						<thead>
							<tr>
								<th data-field="title" data-align="center" data-sortable="true">Subject</th>
								<th data-field="teacher" data-align="center" data-sortable="true">Teacher</th>
								<th data-field="module" data-align="center" data-sortable="true">Module</th>
								<th data-field="note" data-align="center" data-sortable="true">Note</th>
								<% if (currentUser.getRole() > 0) { %>
									<th data-field="view" data-align="center">View</th>
									<th data-field="edit" data-align="center">Edit</th>
									<th data-field="delete" data-align="center">Delete</th>
								<%} %>
							</tr>
						</thead>
						<tbody>
						<% for (TestBean test : tests.get(subject)) { %>
							<tr>
								<td><%= subjects.get(test.getSubjectId()) %></td>
								<td><%= teachers.get(test.getTeacherId()) %></td>
								<td>
									<span class="transformer-text" data-path="tests" data-id=<%= test.getId() %>><%= test.getModule() %></span>
									<input type="text" style="display: none">
								</td>
								<td>
									<span class="transformer-text" data-path="tests" data-id=<%= test.getId() %>>
										<%= (!test.getNote().equals("")) ? test.getNote() : "-" %>
									</span>
									<input type="text" style="display: none">
								</td>
								<% if (currentUser.getRole() > 0) { %>
									<td><a href="tests?id=<%= test.getId() %>">View</a></td>
									<td><a href="test/<%= test.getId() %>">Edit</a></td>
									<td>
										<form action="<%= basePath %>/tests" method="post">
											<button type="submit" class="btn btn-danger">Delete</button>
											<input type="hidden" name="delete-id" value="<%= test.getId() %>">
										</form>
									</td>
								<% } %>
							</tr>
						<% } %>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<% } %>
	</div>
</div>

<%@ include file="footer.jsp"%>
