<%@page import="beans.TestResultBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<TestResultBean> results = (ArrayList<TestResultBean>) request.getAttribute("results"); %>
<% HashMap<Integer, String> groupsMap = (HashMap<Integer, String>) request.getAttribute("groupsMap"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
			<div class="alert alert-${status}">
				<p>${message}</p>
			</div>
		<% } %>
	
		<h1><span class="translate" data-lang-key="Results of the Test"></span></h1>
		<table class="table" data-search="true" data-show-columns="true">
		    <thead>
		        <tr>
		        	<th data-field="number" data-align="center" data-sortable="true">№</th>
		        	<th data-field="group" data-align="center" data-sortable="true">
		        		<span class="translate" data-lang-key="Group"></span>
		        	</th>
		            <th data-field="firstName" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="First Name"></span>
		            </th>
		            <th data-field="lastName" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Last Name"></span>
		            </th>
		            <th data-field="completed" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Completed"></span>
		            </th>
		            <th data-field="result" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Result"></span>
		            </th>
		        </tr>
		    </thead>
		    <tbody>
		    <% int i = 1; %>
		    <% for (TestResultBean result: results) { %>
				<tr>
					<td><%= i %></td>
					<td><%= groupsMap.get(result.getGroupId()) %></td>
					<td><%= result.getFirstName() %></td>
					<td><%= result.getLastName() %></td>
					<td><%= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(result.getCompletedTest()) %></td>
					<td><%= result.getResult() %></td>
			    </tr>
				<% i++; %>
			<% } %>
			</tbody>
		</table>
	</div>
	
<%@ include file="footer.jsp" %>
