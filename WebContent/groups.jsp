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
		<h3 class="lead"><span class="translate" data-lang-key="Add group"></span></h3>

		<form action="<%= basePath %>/groups" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="groupName" class="col-sm-2 control-label"><span class="translate" data-lang-key="Group name"></span>*</label>
				<div class="col-sm-10">
					<input name="groupName" type="text" class="form-control typeahead"
						class="groupName" required autocomplete="off">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 add-subject">
					<button type="submit" class="btn btn-primary"><span class="translate" data-lang-key="Add group"></span></button>
				</div>
			</div>
		</form>
	<% } %>
</div>
	
<div class="container">
	<% if (groups.size() == 0) { %>
		<h1>There are no groups yet</h1>
	<% } else { %>
		<h1><span class="translate" data-lang-key="Groups"></span> (<%= groups.size() %>)</h1>
	<% } %>
	<table class="table" data-search="true" data-show-columns="true">
		<thead>
			<tr>
				<% if (user.getRole() == 2) { %>
				<th data-field="id" data-align="center" data-sortable="true">ID</th>
				<% } %>
				<th data-field="group" data-align="center" data-sortable="true"><span class="translate" data-lang-key="Group"></span></th>
				<th data-field="studentsCount" data-align="center" data-sortable="true"><span class="translate" data-lang-key="Number of students"></span></th>
				<% if (user.getRole() == 2) { %>
				<th data-field="delete" data-align="center"><span class="translate" data-lang-key="Delete"></span></th>
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
			        	<span class="transformer-text" data-path="groups" data-id=<%= group.getId() %>><% out.print(group.getGroupName()); %></span>
			        	<input type="text" style="display: none">
			        </td>
			        <td><%= group.getCountStudents() %></td>
			        <% if (user.getRole() == 2) { %>
			        <td>
						<button type="button" class="btn btn-danger delete-item" data-id=<%= group.getId() %> data-path="/groups">
							<span class="translate" data-lang-key="Delete"></span>
						</button>
			        </td>
			        <% } %>
			    </tr>
			<% } %>
		</tbody>
	</table>
</div>

<%@ include file="footer.jsp" %>
