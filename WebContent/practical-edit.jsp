<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="beans.UserBean"%>
<%@page import="beans.PracticalsBean"%>
<%@page import="beans.FileBean"%>
<%@page import="dao.SubjectsDAO"%>
<%@page import="dao.FileDAO"%>
<%@page import="util.FileUploadManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% saveDir = saveDir.replaceAll("\\\\", "/");; %>
<% PracticalsBean pBean = (PracticalsBean) request.getAttribute("practicalBean"); %>
<% String body = pBean.getBody(); %>
<% ArrayList<FileBean> fileBeans = FileDAO.findAll(pBean.getId(), "practicals"); %>
<% Map<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap(); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	<h3 class="lead"><span class="translate" data-lang-key="Edit practical"></span></h3>
	<form action="<%= basePath %>/practicals" class="form" method="post"
		class="form-horizontal" enctype="multipart/form-data">
		<div class="form-group">
			<label for="subject" class="col-sm-2 control-label">
				<span class="translate" data-lang-key="Subject"></span>*
			</label>
			<div class="col-sm-10">
				<input name="subject" type="text" class="form-control typeahead"
					class="subject" required autocomplete="off" data-autocomplete-url="autocomplete/subjects" 
						value="<%= subjectsMap.get(pBean.getSubjectId()) %>">
			</div>
		</div>

		<div class="form-group">
			<label for="title" class="col-sm-2 control-label">
				<span class="translate" data-lang-key="Title"></span>*
			</label>
			<div class="col-sm-10">
				<input name="title" type="text" class="form-control" id="title" required value="<%= pBean.getTitle() %>">
			</div>
		</div>

		<div class="form-group">
			<label for="body" class="col-sm-2 control-label required">
				<span class="translate" data-lang-key="Body"></span>
			</label>
			<div class="col-sm-10">
				<textarea class="ckeditor" name="body" class="form-control" rows="3"><%= pBean.getBody() %></textarea>
			</div>
		</div>

		<div class="form-group">
			<label for="upload" class="col-sm-2 control-label required">
				<span class="translate" data-lang-key="Upload files"></span>
			</label>
			<div class="col-sm-10">
				<% for (FileBean fileBean : fileBeans) { %>
					<div class="file">
						<div class="file-info">
							<a href="<%= saveDir + File.separator + fileBean.getName() %>" download>
								<img src="imgs/icons/<%= FileUploadManager.extractFileExt(fileBean.getName()) %>.png" 
									alt="<%= fileBean.getName() %>" />
								<%= fileBean.getName() %>
							</a>
						</div>
						<div class="file-delete-button">
							<button type="button" class="btn btn-danger delete-file-btn" 
								data-fid=<%= fileBean.getFid() %>>Delete</button>
						</div>
					</div>
				<% } %>
				<input id="upload" type="file" class="file" name="upload" data-preview-file-type="text" 
					multiple accept="application/msword, application/pdf">
				<p class="help-block">
					<span class="translate" data-lang-key="File size not more then"></span> 10 MB.
					<span class="translate" data-lang-key="Allowed formats"></span>: pdf, doc, docx.
				</p>
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button type="submit" class="btn btn-primary">
					<span class="translate" data-lang-key="Update"></span>
				</button>
			</div>
		</div>
		<input type="hidden" name="update-id" value="<%= pBean.getId() %>">
	</form>
</div>

<%@ include file="footer.jsp" %>
