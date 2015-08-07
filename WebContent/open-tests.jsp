<%@page import="beans.TestBean"%>
<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% TestBean currentTest = (TestBean) request.getAttribute("currentTest"); %>
<% ArrayList<UserBean> students = (ArrayList<UserBean>) request.getAttribute("students"); %>


<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		<div class="page-header">
			<h1>Open the Test</h1>
		</div>

		<form action="<%= basePath %>/openTest" method="post" class="form-horizontal" >
			<div class="form-group">
				<label for="group" class="col-sm-2 control-label required">Group</label>
				<div class="col-sm-10">
					<input type="text" name="group" class="form-control typeahead"
						placeholder="Group" required autocomplete="off"
							data-autocomplete-url="autocomplete/groups">
				</div>
			</div>
				
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="hidden" name="id" value="<%= currentTest.getId() %>">
					<input type="submit" class="btn btn-primary" value="Open a test">
				</div>
			</div>
		</form>
			
			<h1>Students</h1>
			<table class="table" data-search="true" data-show-columns="true">
			<% out.print(students == null ? "Test is not available" : ""); %>
		    <thead>
		        <tr>
		        	<th data-field="number" data-align="center" data-sortable="true">№</th>
		            <th data-field="firstName" data-align="center" data-sortable="true">First Name</th>
		            <th data-field="lastName" data-align="center" data-sortable="true">Last Name</th>
		            <th data-field="delete" data-align="center">Delete</th>
		        </tr>
		    </thead>
		    <tbody>
		    <% int i = 1; %>
		    <% for (UserBean student: students) { %>
				<tr>
					<td><%= i %></td>
			        <td><% out.print(student.getFirstName()); %></td>
			        <td><% out.print(student.getLastName()); %></td>
			        <td><input type="button" class="delete-answer btn btn-danger" value="Delete"></td>
			    </tr>
				<% i++; %>
			<% } %>
			</tbody>
		</table>
	</div>
	
	
	
<%@ include file="footer.jsp" %>
