<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% ArrayList<UserBean> users = (ArrayList<UserBean>) request.getAttribute("usersList"); %>
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
		            <th data-field="userName" data-align="center" data-sortable="true">User Name</th>
		            <th data-field="email" data-align="center" data-sortable="true">Email</th>
		            <th data-field="firstName" data-align="center" data-sortable="true">First Name</th>
		            <th data-field="lastName" data-align="center" data-sortable="true">Last Name</th>
		            <th data-field="role" data-align="center" data-sortable="true">Role</th>
		            <th data-field="edit" data-align="center">Edit</th>
		        </tr>
		    </thead>
		    <tbody>
		    <% for (UserBean user: users) { %>
				<tr>
			        <td><% out.print(user.getUsername()); %></td>
			        <td><% out.print(user.getEmail()); %></td>
			        <td><% out.print(user.getFirstName()); %></td>
			        <td><% out.print(user.getLastName()); %></td>
			        <td><% out.print(user.getHumanRole()); %></td>
			        <td><a href="<% out.print(basePath + "/user/" + user.getId()); %>" target="_blank">Edit</a></td>
			    </tr>
			<% } %>
			</tbody>
		</table>

	</div>
</div>

<%@ include file="footer.jsp" %>
