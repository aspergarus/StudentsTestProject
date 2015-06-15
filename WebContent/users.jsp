<%@page import="beans.UserBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% ArrayList<UserBean> users = (ArrayList<UserBean>) request.getAttribute("usersList"); %>
<% HashMap<Integer, String> departmentsMap = (HashMap<Integer, String>) request.getAttribute("departmentsMap"); %>
<% HashMap<Integer, String> groupsMap = (HashMap<Integer, String>) request.getAttribute("groupsMap"); %>
<% String usersJson = (String) request.getAttribute("usersJson"); %>


<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">

	<div class="starter-template">
		<h1>Users</h1>
		<table class="table" data-search="true" data-show-columns="true">
			<% out.print(users == null ? "Users are not exists" : ""); %>
		    <thead>
		        <tr>
		        	<th data-field="avatar" data-align="center" data-sortable="false">Avatar</th>
		            <th data-field="userName" data-align="center" data-sortable="true">User Name</th>
		            <th data-field="email" data-align="center" data-sortable="true">Email</th>
		            <th data-field="firstName" data-align="center" data-sortable="true">First Name</th>
		            <th data-field="lastName" data-align="center" data-sortable="true">Last Name</th>
		            <th data-field="groupId" data-align="center" data-sortable="true">Group / Department</th>
		            <th data-field="role" data-align="center" data-sortable="true">Role</th>
		            <th data-field="edit" data-align="center">Edit</th>
		        </tr>
		    </thead>
		    <tbody>
		    <% for (UserBean user: users) { %>
				<tr>
					<td><%  if (user.getAvatar().isEmpty()) { %>
					<img src="<%= defaultAvatar %>" class="img-circle avatar-table"><% } else { %>
					<img src="<%= uploadAvatarPath + File.separator + user.getAvatar() %>" class="img-circle avatar-table">
					<% } %>
					</td>
			        <td><% out.print(user.getUsername()); %></td>
			        <td><% out.print(user.getEmail()); %></td>
			        <td><% out.print(user.getFirstName()); %></td>
			        <td><% out.print(user.getLastName()); %></td>
			        <% if (user.getRole() == 0) { %>
			        	<td><% out.print(groupsMap.get(user.getGroupId())); %></td>
			        <% } else if (user.getRole() == 1) { %>
			        	<td><% out.print(departmentsMap.get(user.getGroupId())); %></td>
			        <% } else { %>
			        	<td><% out.print("Administration"); %></td>
			        <% } %>
			        <td><% out.print(user.getHumanRole()); %></td>
			        <td><a href="<% out.print(basePath + "/user/" + user.getId()); %>" target="_blank">Edit</a></td>
			    </tr>
			<% } %>
			</tbody>
		</table>

	</div>
</div>

<%@ include file="footer.jsp" %>
