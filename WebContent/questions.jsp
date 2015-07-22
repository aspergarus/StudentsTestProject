<%@page import="beans.TestBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% TestBean editedTest = (TestBean) request.getAttribute("editedTest"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		<div class="page-header">
			<h1>Add a question</h1>
		</div>

		<form action="<%= basePath %>/test/<%= editedTest.getId() %>" id="question-form" method="post" class="form-horizontal" >
			<div class="form-group">
				<label for="question" class="col-sm-2 control-label required">Question*</label>
				<div class="col-sm-10">
					<textarea name="question" id="question" class="form-control" rows="3" 
						placeholder="Question*" required></textarea>
				</div>
			</div>
 			
 			<div class="answers">

	 			<div class="form-group answer">
					<label for="answer" class="col-sm-2 control-label required">Answer*</label>
					<div class="col-sm-8">
						<input name="answer-1" type="text" class="form-control"
							placeholder="Answer*" required>
					</div>
					<div class="col-sm-1">
						<label>
							<input type="checkbox" name="correct-answer-1" class="true-answer">
							True
						</label>
					</div>
					<div class="col-sm-1">
						<input type="button" class="delete-answer btn btn-danger btn-xs" value="Delete an answer">
					</div>
				</div>
				
				<div class="form-group answer">
					<label for="answer" class="col-sm-2 control-label required">Answer*</label>
					<div class="col-sm-8">
						<input name="answer-2" type="text" class="form-control"
							placeholder="Answer*" required>
					</div>
					<div class="col-sm-1">
						<label>
							<input type="checkbox" name="correct-answer-2" class="true-answer">
							True
						</label>
					</div>
					<div class="col-sm-1">
						<input type="button" class="delete-answer btn btn-danger btn-xs" value="Delete an answer">
					</div>
				</div>
			</div>	
				
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="button" id="add-answer" class="btn btn-primary btn-xs" value="Add an answer">
				</div>
			</div>
				
 			<hr>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="submit" id="add-question" class="btn btn-primary btn-lg" value="Add a question">
				</div>
			</div>
			<input type="hidden" id="count" name="count" value="2">
			<input type="hidden" name="correct-answers" value="">
			<input type="hidden" name="id" value="<%= editedTest.getId() %>">
		</form>
	</div>
	
	

<%@ include file="footer.jsp" %>
