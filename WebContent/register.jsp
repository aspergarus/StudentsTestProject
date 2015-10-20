<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>

<%@ include file="header.jsp"%>
<%@ include file="menu.jsp"%>

<div class="container">

	<% if (status != null && message != null) { %>
	<div class="alert alert-${status}">
		<p>${message}</p>
	</div>
	<% } %>
	<div class="page-header">
		<h1><span class="translate" data-lang-key="User registration"></span></h1>
	</div>

	<form action="register" id="form" method="post" class="form-horizontal">
		<div class="form-group">
			<label for="username" class="col-sm-2 control-label required">
				<span class="translate" data-lang-key="Username"></span>*
			</label>
			<div class="col-sm-10">
				<input name="username" type="text" class="form-control"
					id="username" placeholder="Username*" required>
			</div>
		</div>

		<div class="form-group">
			<label for="pass" class="col-sm-2 control-label required">
				<span class="translate" data-lang-key="Password"></span>*
			</label>
			<div class="col-sm-10">
				<input name="pass" type="password" class="form-control" id="pass"
					placeholder="Password*" required>
			</div>
		</div>

		<div class="form-group">
			<label for="username" class="col-sm-2 control-label required">
				<span class="translate" data-lang-key="Email"></span>*
			</label>
			<div class="col-sm-10">
				<input name="email" type="email" class="form-control"
					placeholder="Email*" required>
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<div class="radiobutton">
					<label> <input class="radio-user-status" type="radio" name="role" value="0" checked>
						<span class="translate" data-lang-key="Student"></span>
					</label>
				</div>
			</div>
			<div class="col-sm-offset-2 col-sm-10">
				<div class="radiobutton">
					<label> <input class="radio-user-status" type="radio" name="role" value="1">
						<span class="translate" data-lang-key="Teacher"></span>
					</label>
				</div>
			</div>
			<div class="col-sm-offset-2 col-sm-10">
				<div class="radiobutton">
					<label> <input class="radio-user-status" type="radio" name="role" value="2">
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
					id="firstname" placeholder="First Name" required>
			</div>
		</div>

		<div class="form-group">
			<label for="lastname" class="col-sm-2 control-label required">
				<span class="translate" data-lang-key="Last Name"></span>
			</label>
			<div class="col-sm-10">
				<input name="lastname" type="text" class="form-control"
					id="lastname" placeholder="Last Name" required>
			</div>
		</div>
		
		<div class="form-group">
			<label for="group" class="col-sm-2 control-label required">
			<span class="translate" data-lang-key="Group"></span> / <span class="translate" data-lang-key="Department"></span> 
			 </label>
			<div class="col-sm-10">
				<input name="group" type="text" class="form-control typeahead"
					id="group-autocomplete" placeholder="Group" required
						autocomplete="off" data-autocomplete-url="autocomplete/groups">
				<input name="department" type="text" class="form-control typeahead hidden"
					id="department-autocomplete" placeholder="Department"
						autocomplete="off" data-autocomplete-url="autocomplete/departments">
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button type="submit" class="btn btn-primary">
					<span class="translate" data-lang-key="Register"></span>
				</button>
			</div>
		</div>
	</form>
</div>

<%@ include file="footer.jsp"%>
