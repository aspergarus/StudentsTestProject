<%@page import="java.io.File"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.LecturesBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% saveDir = saveDir.replaceAll("\\\\", "/");; %>
<% LecturesBean lBean = (LecturesBean) request.getAttribute("lecturesBean"); %>
<% String body = lBean.getBody(); %>
<% String fileName = lBean.getFileName(); %>
<% String fileExt = (fileName.lastIndexOf(".") > 0) ? fileName.substring(fileName.lastIndexOf(".") + 1) : "_blank"; %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
	<div class="alert alert-${status}">
		<p>${message}</p>
	</div>
	<% } %>
	<h3 class="lead">Edit lecture</h3>
	<form action="<%= basePath %>/lectures" class="form" method="post"
		class="form-horizontal" enctype="multipart/form-data">
		<div class="form-group">
			<label for="subject" class="col-sm-2 control-label">Subject*</label>
			<div class="col-sm-10">
				<input name="subject" type="text" class="form-control typeahead"
					class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/practicalSubjects" value="<%= lBean.getSubject() %>">
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">Title*</label>
			<div class="col-sm-10">
				<input name="title" type="text" class="form-control" id="title" required value="<%= lBean.getTitle() %>">
			</div>
		</div>

		<div class="form-group">
			<label for="body" class="col-sm-2 control-label required">Body</label>
			<div class="col-sm-10">
				<textarea name="body" class="form-control" rows="3"><%= lBean.getBody() %></textarea>
			</div>
		</div>

		<div class="form-group">
			<label for="upload" class="col-sm-2 control-label required">Upload files</label>
			<div class="col-sm-10">
				<% if (!fileName.isEmpty()) { %>
					<div class="file">
						<a href="<%= saveDir + "/" + fileName %>">
						<img src="imgs/icons/<%= fileExt %>.png" alt="<%= fileName %>" />
						<%= fileName %>
						</a>
					</div>
				<% } %>
				<input type="file" value="<%= fileName %>" name="upload" id="upload" class="form-control" accept="application/msword, application/pdf">
				<p class="help-block">File size not more then 10 MB. Allowed formats: pdf, doc, docx.</p>
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button type="submit" class="btn btn-primary">Update</button>
			</div>
		</div>
		<input type="hidden" name="update-id" value="<%= lBean.getId() %>">
	</form>
</div>

<%@ include file="footer.jsp" %>
