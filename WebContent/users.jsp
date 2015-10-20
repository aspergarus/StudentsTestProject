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
		<h1><span class="translate" data-lang-key="Users"></span></h1>
		<table class="table" data-search="true" data-show-columns="true">
			<%= (users == null) ? "Users are not exists" : "" %>
		    <thead>
		        <tr>
		        	<th data-field="avatar" data-align="center" data-sortable="false">
		        		<span class="translate" data-lang-key="Avatar"></span>
		        	</th>
		            <th data-field="userName" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Username"></span>
		            </th>
		            <th data-field="email" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Email"></span>
		            </th>
		            <th data-field="firstName" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="First Name"></span>
		            </th>
		            <th data-field="lastName" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Last Name"></span>
		            </th>
		            <th data-field="groupId" data-align="center" data-sortable="true">
		            <span class="translate" data-lang-key="Group"></span> / 
		            <span class="translate" data-lang-key="Department"></span>
		            </th>
		            <th data-field="registered" data-align="center" data-sortable="true">
		            <span class="translate" data-lang-key="Registration Date"></span>
		            </th>
		            <th data-field="role" data-align="center" data-sortable="true">
		            	<span class="translate" data-lang-key="Role"></span>
		            </th>
		            <th data-field="edit" data-align="center">
		            	<span class="translate" data-lang-key="Edit"></span>	
		            </th>
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
			        	<td><span class="translate" data-lang-key="Administration"></span></td>
			        <% } %>
			        <td>
			        	<%= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(user.getRegistered()) %>
			        </td>
			        <td><%= user.getHumanRole() %></td>
			        <td><a href="<%= basePath + "/user/" + user.getId() %>" target="_blank">
			        	<span class="translate" data-lang-key="Edit"></span>
			        </a></td>
			    </tr>
			<% } %>
			</tbody>
		</table>
	</div>
</div>

<%@ include file="footer.jsp" %>
