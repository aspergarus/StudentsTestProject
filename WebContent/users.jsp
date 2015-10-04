<%@page import="beans.UserBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp" %>
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
			<%= (users == null) ? "Users are not exists" : "" %>
		    <thead>
		        <tr>
		        	<th data-field="avatar" data-align="center" data-sortable="false">Avatar</th>
		            <th data-field="userName" data-align="center" data-sortable="true">User Name</th>
		            <th data-field="email" data-align="center" data-sortable="true">Email</th>
		            <th data-field="firstName" data-align="center" data-sortable="true">First Name</th>
		            <th data-field="lastName" data-align="center" data-sortable="true">Last Name</th>
		            <th data-field="groupId" data-align="center" data-sortable="true">Group / Department</th>
		            <th data-field="registered" data-align="center" data-sortable="true">Registration Date</th>
		            <th data-field="role" data-align="center" data-sortable="true">Role</th>
		            <th data-field="edit" data-align="center">Edit</th>
		        </tr>
		    </thead>
		    <tbody>
		    <% for (UserBean user: users) { %>
				<tr>
					<td>
						<%  if (user.getAvatar().isEmpty()) { %>
						<img src="<%= defaultAvatar %>" class="img-circle avatar-table"><% } else { %>
						<img src="<%= uploadAvatarPath + File.separator + user.getAvatar() %>" class="img-circle avatar-table">
						<% } %>
					</td>
			        <td><%= user.getUsername() %></td>
			        <td><%= user.getEmail() %></td>
			        <td><%= user.getFirstName() %></td>
			        <td><%= user.getLastName() %></td>
			        <% if (user.getRole() == 0) { %>
			        	<td><%= groupsMap.get(user.getGroupId()) %></td>
			        <% } else if (user.getRole() == 1) { %>
			        	<td><%= departmentsMap.get(user.getGroupId()) %></td>
			        <% } else { %>
			        	<td><%= "Administration" %></td>
			        <% } %>
			        <td>
			        	<%= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(user.getRegistered()) %>
			        </td>
			        <td><%= user.getHumanRole() %></td>
			        <td><a href="<%= basePath + "/user/" + user.getId() %>" target="_blank">Edit</a></td>
			    </tr>
			<% } %>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="footer.jsp" %>
