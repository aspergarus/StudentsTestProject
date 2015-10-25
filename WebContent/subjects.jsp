<%@page import="beans.SubjectsBean"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% ArrayList<SubjectsBean> subjects = (ArrayList<SubjectsBean>) request.getAttribute("subjectsList"); %>
<% HashMap<Integer, String> departmentsMap = (HashMap<Integer, String>) request.getAttribute("departmentsMap"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<% if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	
	<% if (user.getRole() == 2) { %>
		
		<h3 class="lead"><span class="translate" data-lang-key="Add subject"></span></h3>

		<form action="<%= basePath %>/subjects" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="subjectName" class="col-sm-2 control-label">
					<span class="translate" data-lang-key="Subject name"></span>*
				</label>
				<div class="col-sm-10">
					<input name="subjectName" type="text" class="form-control subjectName"
						required autocomplete="off">
				</div>
			</div>
			<div class="form-group">
				<label for="departmentName" class="col-sm-2 control-label">
					<span class="translate" data-lang-key="Department"></span>*
				</label>
				<div class="col-sm-10">
					<input name="departmentName" type="text" class="form-control typeahead" id="departmentName" required
						autocomplete="off" data-autocomplete-url="autocomplete/departments">
				</div>
			</div>	
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 add-subject">
					<button type="submit" class="btn btn-primary">
						<span class="translate" data-lang-key="Add"></span>
					</button>
				</div>
			</div>
		</form>
	<% } %>
</div>
	
<div class="container">
	<% if (subjects.size() == 0) { %>
		<h1><span class="translate" data-lang-key="There are no subjects yet"></span></h1>
	<% } else { %>
		<h1><span class="translate" data-lang-key="Subjects"></span> (<%= subjects.size() %>)</h1>
	<% } %>
	<table class="table" data-search="true" data-show-columns="true">
		<thead>
			<tr>
				<% if (user.getRole() == 2) { %>
					<th data-field="id" data-align="center" data-sortable="true">ID</th>
				<% } %>
				<th data-field="subjectName" data-align="center" data-sortable="true">
					<span class="translate" data-lang-key="Subject name"></span>
				</th>
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
		    <% for (SubjectsBean subject: subjects) { %>
				<tr>
					<% if (user.getRole() == 2) { %>
			        	<td><% out.print(subject.getId()); %></td>
			        <% } %>
			        <td>
			        	<span class="transformer-text" data-path="subjects" data-id=<%= subject.getId() %>>
			        		<%= subject.getSubjectName() %>
			        	</span>
			        	<input type="text" style="display: none">
			        </td>
			        <td><% out.print (departmentsMap.get(subject.getDepartmentId())); %></td>
			        <% if (user.getRole() == 2) { %>
				        <td>
							<button class="btn btn-danger delete-item" data-id="<%= subject.getId() %>"
									data-path="/subjects" data-item="subject">
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
