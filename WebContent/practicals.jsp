<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="beans.PracticalsBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean currentUser = (UserBean) request.getAttribute("currentUser"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% HashMap<String, ArrayList<PracticalsBean>> practicalsMap = (HashMap<String, ArrayList<PracticalsBean>>) request.getAttribute("practicalsMap"); %>
<% HashMap<String, String> groups = (HashMap<String, String>) request.getAttribute("groupsMap"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
	<div class="alert alert-${status}">
		<p>${message}</p>
	</div>
	<% } %>
	
	<% if (currentUser.getRole() > 0) { %>
	
		<h3 class="lead">Add practical</h3>
		<form action="<%= basePath %>/practicals" class="form" method="post"
			class="form-horizontal" enctype="multipart/form-data">
			<div class="form-group">
				<label for="subject" class="col-sm-2 control-label">Subject*</label>
				<div class="col-sm-10">
					<input name="subject" type="text" class="form-control typeahead"
						class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/subjects">
				</div>
			</div>
	
			<div class="form-group">
				<label for="title" class="col-sm-2 control-label">Title*</label>
				<div class="col-sm-10">
					<input name="title" type="text" class="form-control" id="title" required>
				</div>
			</div>
	
			<div class="form-group">
				<label for="body" class="col-sm-2 control-label required">Body</label>
				<div class="col-sm-10">
					<textarea class="ckeditor" name="body" class="form-control" rows="3"></textarea>
				</div>
			</div>
	
			<div class="form-group">
				<label for="upload" class="col-sm-2 control-label required">Upload files</label>
				<div class="col-sm-10">
					<input id="upload" type="file" class="file" name="upload" data-preview-file-type="text" multiple>
					<p class="help-block">File size not more then 10 MB. Allowed formats: pdf, doc, docx.</p>
				</div>
			</div>
	
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Add practical</button>
				</div>
			</div>
		</form>
	<% } %>
</div>

<div class="container">
	<% if (practicalsMap.size() == 0) { %>
		<% if (currentUser.getRole() == 0) { %>
			<h1>We don't have practicals for you.</h1>
		<% } else if (currentUser.getRole() == 1) { %>
			<h1>You don't have practicals.</h1>
			<h2><small>You can add them on the form over this message.</small></h2>
		<% } else { %>
			<h1>There are no any practicals</h1>
		<% } %>
	<% } else { %>
		<h1>Practicals (<%= practicalsMap.size() %>)</h1>
	<% } %>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
		<% int i = 0; %>
		<% for (String subject : practicalsMap.keySet()) { %>
		<% i++; %>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="heading-<%= i %>">
				<h4 class="panel-title">
					<a class="subject-<%= i %>" data-toggle="collapse" data-parent="#accordion" href="#collapse-<%= i %>" aria-controls="collapse-<%= i %>">
					  <%= subject %>
					</a>
				</h4>
			</div>
			<div id="collapse-<%= i %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-<%= i %>">
				<div class="panel-body">
				
				<% if (currentUser.getRole() == 1) { %>
					<p class="help-block">Share this subject to groups:
						<input type="text" class="tokenfield" value="<%= groups.get(subject) == null ? "" :  groups.get(subject) %>" name="groupName" required autocomplete="off" />
						<input type="submit" value="Share" class="btn btn-info btn-share assign-subject-group" data-num="<%= i %>" data-subject="<%= subject %>">
					</p>
				<% } %>
					
					<table class="table">
						<thead>
							<tr>
								<th data-field="title" data-align="center" data-sortable="true">Title</th>
					            <th data-field="view" data-align="center">View</th>
					            <% if (currentUser.getRole() > 0) { %>
					            	<th data-field="edit" data-align="center">Edit</th>
					            	<th data-field="delete" data-align="center">Delete</th>
					            <%} %>
							</tr>
						</thead>
						<tbody>
						<% for (PracticalsBean practical : practicalsMap.get(subject)) { %>
							<tr>
								<td><%= practical.getTitle() %></td>
								<td><a href="practicals?id=<%= practical.getId() %>">View</a></td>
								<% if (currentUser.getRole() > 0) { %>
									<td><a href="practicals?edit=true&id=<%= practical.getId() %>">Edit</a></td>
									<td>
										<button class="btn btn-danger delete-item" data-id="<%= practical.getId() %>" 
											data-path="/practicals">Delete</button>
									</td>
								<% } %>
							</tr>
						<% } %>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<% } %>
	</div>
</div>

<%@ include file="footer.jsp" %>
