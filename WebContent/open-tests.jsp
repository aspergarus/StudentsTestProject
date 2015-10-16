<%@page import="beans.TestBean"%>
<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% TestBean currentTest = (TestBean) request.getAttribute("currentTest"); %>
<% ArrayList<UserBean> students = (ArrayList<UserBean>) request.getAttribute("students"); %>
<% ArrayList<Integer> testStudentsId = (ArrayList<Integer>) request.getAttribute("testStudentsId"); %>
<% HashMap<Integer, String> groupsMap = (HashMap<Integer, String>) request.getAttribute("groupsMap"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		<div class="page-header">
			<h2>Add students to list</h2>
		</div>

		<form action="<%= basePath %>/openTest" method="post" class="form-horizontal" >
			<div class="form-group">
				<label for="group" class="col-sm-2 control-label required">Group</label>
				<div class="col-sm-10">
					<input type="text" name="group" class="form-control typeahead"
						placeholder="Group" required autocomplete="off"
							data-autocomplete-url="autocomplete/assignedGroups/<%= currentTest.getSubjectId() %>">
				</div>
			</div>
				
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="hidden" name="id" value="<%= currentTest.getId() %>">
					<input type="submit" class="btn btn-primary" value="Add Group">
				</div>
			</div>
		</form>
		
		<form action="<%= basePath %>/openTest" method="post" class="form-horizontal" >
			<div class="form-group">
				<label for="student" class="col-sm-2 control-label required">Student</label>
				<div class="col-sm-10">
					<input type="text" name="student" class="form-control typeahead"
						placeholder="Students first or last name" required autocomplete="off"
							data-autocomplete-url="autocomplete/assignedStudents/<%= currentTest.getSubjectId() %>">
				</div>
			</div>
				
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="hidden" name="id" value="<%= currentTest.getId() %>">
					<input type="submit" class="btn btn-primary" value="Add Student">
				</div>
			</div>
		</form>
			
			<h1>Students</h1>
			<table class="table" data-search="true" data-show-columns="true">
			<% out.print(students == null ? "Test is not available" : ""); %>
		    <thead>
		        <tr>
		        	<th data-field="number" data-align="center" data-sortable="true">№</th>
		        	<th data-field="group" data-align="center" data-sortable="true">Group</th>
		            <th data-field="firstName" data-align="center" data-sortable="true">First Name</th>
		            <th data-field="lastName" data-align="center" data-sortable="true">Last Name</th>
		            <th data-field="select" data-align="center">Select</th>
		            <th data-field="delete" data-align="center">Remove</th>
		        </tr>
		    </thead>
		    <tbody>
		    <% int i = 1; %>
		    <% for (UserBean student: students) { %>
				<tr>
					<td><%= i %></td>
					<td><% out.print(groupsMap.get(student.getGroupId())); %></td>
			        <td><% out.print(student.getFirstName()); %></td>
			        <td><% out.print(student.getLastName()); %></td>
			        <td>
				        <% if (testStudentsId.contains(student.getId())) { %>
				        	<input type="checkbox" class="selected-student" checked>
				        <% } else { %>
				        	<input type="checkbox" class="selected-student">
				        <% } %>
				        <input type="hidden" value="<%= student.getId() %>">
			        </td>
			        <td>
			        	<button type="button" class="btn btn-danger remove-student">
							<span class="translate" data-lang-key="Delete"></span>
						</button>
						<input type="hidden" value="<%= student.getId() %>">
					</td>
			    </tr>
				<% i++; %>
			<% } %>
			<tr>
				<td></td>
				<td></td>
		        <td></td>
		        <td></td>
		        <td>
		        	<label for="toggle-select">Select all</label>
				    <input type="checkbox" id="toggle-select">
			  	</td>
		        <td><input id="clear-ready-students" type="button" class="btn btn-danger" value="Remove all"></td>
			</tr>
			</tbody>
		</table>
		<div class="col-sm-12">
			<input id="open-test-to-students" type="button" class="btn btn-info" value="Apply">
			
		</div>
	</div>
	
	
	
<%@ include file="footer.jsp" %>
