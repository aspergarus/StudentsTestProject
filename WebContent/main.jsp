<%@page import="beans.UserBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% Byte userRole = (Byte) request.getAttribute("userRole"); %>
<% String status = (String) request.getAttribute("status"); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>
<% UserBean currentUser = (UserBean) request.getAttribute("currentUser"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<div class="main-page">
		<div class="col-xs-6 col-sm-6 col-md-3">
			<a href="<%= basePath %>/lectures" class="thumbnail">
				<img src="imgs/lectures.jpg" alt="Lections" />
				<h2><small><span class="translate" data-lang-key="Lectures"></span></small></h2>
			</a>
		</div>
		
		<div class="col-xs-6 col-sm-6 col-md-3">
			<a href="<%= basePath %>/practicals" class="thumbnail">
				<img src="imgs/practicals.jpg" alt="Practical" />
				<h2><small><span class="translate" data-lang-key="Practicals"></span></small></h2>
			</a>
		</div>
		
		<div class="col-xs-6 col-sm-6 col-md-3">
			<a href="<%= basePath %>/tests" class="thumbnail">
				<img src="imgs/tests.jpg" alt="Tests" />
				<h2><small><span class="translate" data-lang-key="Tests"></span></small></h2>
			</a>
		</div>
		<% if (currentUser.getRole() > 0) { %>
			<div class="col-xs-6 col-sm-6 col-md-3">
				<a href="<%= basePath %>/students" class="thumbnail">
					<img src="imgs/students.jpg" alt="Students" />
					<h2><small><span class="translate" data-lang-key="Students"></span></small></h2>
				</a>
			</div>
		<% } %>
	</div>
</div>

<%@ include file="footer.jsp" %>
