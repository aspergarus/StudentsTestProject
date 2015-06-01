<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="beans.LecturesBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean currentUser = (UserBean) request.getAttribute("currentUser"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% Map<String, ArrayList<LecturesBean>> lecturesMap = (HashMap<String, ArrayList<LecturesBean>>) request.getAttribute("lecturesMap"); %>

<%@ include file="header.jsp"%>

<%@ include file="menu.jsp"%>
	
<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
		
	<% if (currentUser.getRole() > 0) { %>
		
		<h3 class="lead">Add lecture</h3>

		<form action="<%= basePath %>/lectures" class="form" method="post"
			class="form-horizontal" enctype="multipart/form-data">
			<div class="form-group">
				<label for="subject" class="col-sm-2 control-label">Subject*</label>
				<div class="col-sm-10">
					<input name="subject" type="text" class="form-control typeahead"
						class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/lecturesSubjects">
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
					<textarea name="body" class="form-control" rows="3"></textarea>
				</div>
			</div>
	
			<div class="form-group">
				<label for="upload" class="col-sm-2 control-label required">Upload files</label>
				<div class="col-sm-10">
					<div class="fileinput fileinput-new input-group" data-provides="fileinput">
  						<div class="form-control" data-trigger="fileinput"><i class="glyphicon glyphicon-file fileinput-exists"></i> <span class="fileinput-filename"></span></div>
  						<span class="input-group-addon btn btn-default btn-file"><span class="fileinput-new">Select file</span><span class="fileinput-exists">Change</span><input type="file" name="upload"></span>
  						<a href="#" class="input-group-addon btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
					</div>
					<p class="help-block">File size not more then 10 MB. Allowed formats: pdf, doc, docx.</p>
				</div>
			</div>
	
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Add lecture</button>
				</div>
			</div>
		</form>
	<% } %>
</div>

<div class="container">
	<h1>Lectures</h1>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
		<% int i = 0; %>
		<% for (String subject : lecturesMap.keySet()) { %>
		<% i++; %>
		<div class="panel panel-default">
			<div class="panel-heading" role="tab" id="heading-<%= i %>">
				<h4 class="panel-title">
					<a data-toggle="collapse" data-parent="#accordion" href="#collapse-<%= i %>" aria-controls="collapse-<%= i %>">
					  <%= subject %>
					</a>
				</h4>
			</div>

			<div id="collapse-<%= i %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-<%= i %>">
				<div class="panel-body">
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
					    <% for (LecturesBean lecture : lecturesMap.get(subject)) { %>
					    	<tr>
								<td><%= lecture.getTitle() %></td>
						        <td><a href="lectures?id=<%= lecture.getId() %>">View</a></td>
						        <% if (currentUser.getRole() > 0) { %>
						        <td><a href="lectures/<%= lecture.getId() %>/edit">Edit</a></td>
						        <td>
						        	<form action="<%= basePath %>/lectures" method="post">
										<button type="submit" class="btn btn-danger">Delete</button>
										<input type="hidden" name="delete-id" value="<%= lecture.getId() %>">
									</form>
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

<%@ include file="footer.jsp"%>
