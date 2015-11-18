<%@page import="beans.DepartmentBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<DepartmentBean> departments = (ArrayList<DepartmentBean>) request.getAttribute("departmentsList"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	<% if (user.getRole() == 2) { %>	
		<h3 class="lead"><span class="translate" data-lang-key="Add department"></span></h3>

		<form action="<%= basePath %>/departments" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="departmentName" class="col-sm-2 control-label">
					<span class="translate" data-lang-key="Department name"></span>*
				</label>
				<div class="col-sm-10">
					<input name="departmentName" type="text" class="form-control"
						class="departmentName" required autocomplete="off">
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 add-subject">
					<button type="submit" class="btn btn-primary"><span class="translate" data-lang-key="Add"></span></button>
				</div>
			</div>
		</form>
	<% } %>
</div>
	
<div class="container">
	<% if (departments.size() == 0) { %>
		<h1><span class="translate" data-lang-key="There are no departments yet"></span></h1>
	<% } else { %>
		<h1>
			<span class="translate departments-title-form" data-lang-key="Departments"></span>
			(<span class="item-number"><%= departments.size() %></span>)
		</h1>
	<% } %>
	<table class="table" data-search="true" data-show-columns="true" data-unique-id="id">
		<thead>
			<tr>
				<% if (user.getRole() == 2) { %>
					<th data-field="id" data-align="center" data-sortable="true">ID</th>
				<% } %>
					<th data-field="department" data-align="center" data-sortable="true">
						<span class="translate" data-lang-key="Department"></span>
					</th>
				<% if (user.getRole() == 2) { %>
					<th data-field="delete" data-align="center">
						<span class="translate" data-lang-key="Delete"></span>
					</th>
				<% } %>
			</tr>
		</thead>
		<tbody>
		    <% for (DepartmentBean department: departments) { %>
				<tr class="department-record">
					<% if (user.getRole() == 2) { %>
			        	<td><% out.print(department.getId()); %></td>
			        <% } %>
			        <td>
			        	<% if (user.getRole() == 2) { %>
				        	<span  class="transformer-text" data-path="departments" data-id=<%= department.getId() %>>
				        		<% out.print(department.getDepartmentName()); %>
				        	</span>
				        	<input type="text" style="display: none">
			        	<% } else { %>
			        		<% out.print(department.getDepartmentName()); %>
			        	<% } %>
			        </td>
			        <% if (user.getRole() == 2) { %>
				        <td>
							<button type="button" class="btn btn-danger delete-item" data-id=<%= department.getId() %>
									data-path="/departments" data-item="department">
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
