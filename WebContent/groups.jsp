<%@page import="beans.GroupBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<GroupBean> groups = (ArrayList<GroupBean>) request.getAttribute("groupsList"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	<% if (user.getRole() == 2) { %>	
		<h3 class="lead">Add group</h3>

		<form action="<%= basePath %>/groups" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="groupName" class="col-sm-2 control-label">Group name*</label>
				<div class="col-sm-10">
					<input name="groupName" type="text" class="form-control typeahead"
						class="groupName" required autocomplete="off">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 add-subject">
					<button type="submit" class="btn btn-primary">Add Group</button>
				</div>
			</div>
		</form>
	<% } %>
</div>
	
<div class="container">
	<h1>Groups</h1>
	<table class="table" data-search="true" data-show-columns="true">
		<% out.print(groups == null ? "Groups are not exists" : ""); %>
		<thead>
			<tr>
				<% if (user.getRole() == 2) { %>
				<th data-field="id" data-align="center" data-sortable="true">ID</th>
				<% } %>
				<th data-field="group" data-align="center" data-sortable="true">Group</th>
				<% if (user.getRole() == 2) { %>
				<th data-field="delete" data-align="center">Delete</th>
				<% } %>
			</tr>
		</thead>
		<tbody>
		    <% for (GroupBean group: groups) { %>
				<tr class="group-record">
					<% if (user.getRole() == 2) { %>
			        <td><% out.print(group.getId()); %></td>
			        <% } %>
			        <td>
			        	<span class="transformer-text-group" data-id=<%= group.getId() %>><% out.print(group.getGroupName()); %></span>
			        	<input type="text" style="display: none">
			        </td>
			        <% if (user.getRole() == 2) { %>
			        <td>
						<button type="button" class="btn btn-danger delete-group-btn" data-id=<%= group.getId() %>>Delete</button>
			        </td>
			        <% } %>
			    </tr>
			<% } %>
		</tbody>
	</table>
</div>

<%@ include file="footer.jsp" %>
