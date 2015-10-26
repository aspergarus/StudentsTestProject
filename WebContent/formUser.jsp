<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% UserBean editedUser = (UserBean) request.getAttribute("editedUser"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
			<div class="alert alert-${status}">
				<p>${message}</p>
			</div>
		<% } %>
		<div class="page-header">
			<h1><span class="translate" data-lang-key="Edit profile"></span></h1>
		</div>

		<form action="<%= basePath %>/user/<%= editedUser.getId() %>" id="form" method="post" enctype="multipart/form-data"
				class="form-horizontal">
			<div class="form-group">
				<label for="editedusername" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Username"></span>*
				</label>
				<div class="col-sm-10">
					<input name="editedusername" type="text" class="form-control"
						id="editedusername" placeholder="Username*" required value="<%= editedUser.getUsername() %>">
				</div>
			</div>
 
			<div class="form-group">
				<label for="editedpass" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Password"></span>*
				</label>
				<div class="col-sm-10">
					<input name="editedpass" type="password" class="form-control" id="editedpass"
						placeholder="You can leave the old password. Just don't fill this field">
				</div>
			</div>

			<div class="form-group">
				<label for="editedemail" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Email"></span>*
				</label>
				<div class="col-sm-10">
					<input name="editedemail" type="email" class="form-control"
						placeholder="Email*" required value=<%= editedUser.getEmail() %>>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<div class="radiobutton">
						<label> <input type="radio" name="role" value="0" <%= editedUser.getRole() == 0 ? "checked" : "" %>>
							<span class="translate" data-lang-key="Student"></span>
						</label>
					</div>
				</div>
				<div class="col-sm-offset-2 col-sm-10">
					<div class="radiobutton">
						<label> <input type="radio" name="role" value="1" <%= editedUser.getRole() == 1 ? "checked" : "" %>>
							<span class="translate" data-lang-key="Teacher"></span>
						</label>
					</div>
				</div>
				<div class="col-sm-offset-2 col-sm-10">
					<div class="radiobutton">
						<label> <input type="radio" name="role" value="2" <%= editedUser.getRole() == 2 ? "checked" : "" %>>
							<span class="translate" data-lang-key="Admin"></span>
						</label>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="firstname" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="First Name"></span>
				</label>
				<div class="col-sm-10">
					<input name="firstname" type="text" class="form-control"
						id="firstname" placeholder="First Name" required 
							value="<%= editedUser.getFirstName() == null ? "" : editedUser.getFirstName() %>">
				</div>
			</div>

			<div class="form-group">
				<label for="lastname" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Last Name"></span>
				</label>
				<div class="col-sm-10">
					<input name="lastname" type="text" class="form-control"
						id="lastname" placeholder="Last Name" required 
							value="<%= editedUser.getLastName() == null ? "" : editedUser.getLastName() %>">
				</div>
			</div>
			
			<div class="form-group">
				<label for="avatar" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Avatar"></span>
				</label>
				<div class="col-sm-10">
					<% if (editedUser.getAvatar().isEmpty()) { %>
						<img src="<%= defaultAvatar %>" class="img-circle avatar-form">
					<% } else { %>
						<img src="<%= uploadAvatarPath + File.separator + editedUser.getAvatar() %>" class="img-circle avatar-form">
					<% } %>
					<div class="fileinput fileinput-new" data-provides="fileinput">
  						<span class="btn btn-default btn-file">
  							<span class="fileinput-new"><span class="translate" data-lang-key="Select file"></span></span>
  							<span class="fileinput-exists"><span class="translate" data-lang-key="Change"></span></span>
  							<input type="file" name="avatar">
  						</span>
  						<span class="fileinput-filename"></span>
  						<a href="#" class="close fileinput-exists" data-dismiss="fileinput" style="float: none">&times;</a>
					</div>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">
						<span class="translate" data-lang-key="Save changes"></span>
					</button>
				</div>
			</div>
			<input type="hidden" name="id" value="<%= editedUser.getId() %>">
		</form>
	</div>

<%@ include file="footer.jsp" %>
