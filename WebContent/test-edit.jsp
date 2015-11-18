<%@page import="beans.TestBean"%>
<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="beans.FileBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.FileUploadManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% saveDir = saveDir.replaceAll("\\\\", "/"); %>
<% TestBean editedTest = (TestBean) request.getAttribute("editedTest"); %>
<% ArrayList<QuestionBean> questions = (ArrayList<QuestionBean>) request.getAttribute("questions"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
			<div class="alert alert-${status}">
				<p>${message}</p>
			</div>
		<% } %>
		<p class="go-back"><a href="<%= basePath %>/tests">back to tests</a></p>
		<div class="page-header">
			<h1><span class="translate" data-lang-key="Add a question"></span></h1>
		</div>

		<form action="<%= basePath %>/test/<%= editedTest.getId() %>" id="question-form" method="post"
			class="form-horizontal" enctype="multipart/form-data">
			<div class="form-group">
				<label for="question" class="col-xs-12 col-sm-2 col-md-2 control-label required">
					<span class="translate" data-lang-key="Question"></span>*
				</label>
				<div class="col-xs-12 col-sm-10 col-md-10">
					<textarea name="question" id="question" class="form-control" rows="3" 
						placeholder="Question*" required></textarea>
				</div>
			</div>
			
			<div class="form-group">
				<label for="upload" class="col-xs-12 col-sm-2 col-md-2 control-label required">
					<span class="translate" data-lang-key="Upload image"></span>
				</label>
				<div class="col-xs-12 col-sm-10 col-md-10">
					<input id="upload" type="file" class="file" name="upload" data-preview-file-type="picture">
					<p class="help-block">
						<span class="translate" data-lang-key="File size not more then"></span> 10 MB.
					</p>
				</div>
			</div>
 			
 			<div class="answers">
				<% for (int k = 1; k <= 2; k++) { %>
		 			<div class="form-group answer">
						<label for="answer" class="col-xs-2 col-sm-2 col-md-2 control-label required">
							<span class="translate" data-lang-key="Answer"></span>*
						</label>
						<div class="col-xs-6 col-sm-8 col-md-8">
							<input name="answer-<%= k %>" type="text" class="form-control"
								placeholder="Answer*" required>
						</div>
						<div class="col-xs-2 col-sm-1 col-md-1 correct-answer-label">
							<label>
								<input type="checkbox" name="correct-answer-<%= k %>" class="true-answer">
								<span class="translate" data-lang-key="Correct"></span>
							</label>
						</div>
						<div class="col-xs-2 col-sm-1 col-md-1">
							<input type="button" class="delete-answer btn btn-danger btn-xs" value="Delete">
						</div>
					</div>
				<% } %>
				
			</div>	
				
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 col-md-offset-2 col-md-10">
					<button type="button" id="add-answer" class="btn btn-primary btn-xs">
						<span class="translate" data-lang-key="Add answer"></span>
					</button>
				</div>
			</div>
				
 			<hr>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10 col-md-offset-2 col-md-10">
					<div class="tooltip-element" data-toggle="tooltip" data-placement="right" 
						title="Запитання повинне мати хоча б одну правильну відповідь">
						<button type="submit" id="add-question" class="btn btn-primary btn-lg">
							<span class="translate" data-lang-key="Add a question"></span>
						</button>
					</div>
				</div>
			</div>
			<input type="hidden" id="count" name="count" value="2">
			<input type="hidden" name="correct-answers" value="">
			<input type="hidden" name="id" value="<%= editedTest.getId() %>">
		</form>
	</div>
	
	<div class="container">
	<h1><span class="translate" data-lang-key="Added questions"></span>: <%= questions.size() %></h1>
	
	<div class="panel panel-default">
		<% int i = 0; %>
		<% for (QuestionBean question : questions) { %>
		<% i++; %>
  			<div class="panel-heading">
	  			<form action="<%= basePath %>/test/<%= editedTest.getId() %>" method="post">
	    			<h3 class="panel-title"><%= i + ". " + question.getQuestionText() %>
	    				<input type="hidden" name="delete-question-id" value="<%= question.getId() %>">
	    				<input class="submit-delete-question hidden" type="submit">
	    				<a class="delete-question" href="#"><span class="glyphicon glyphicon-remove-circle delete-question" aria-hidden="true"></span></a>
	    			</h3>
	    		</form>
  			</div>
  			<div class="panel-body">
  				<% FileBean image = question.getImage(); %>
  				<% if (image != null) { %>
	  				<div class="col-md-12 img-container">
	  					<img class="question-img" src="<%= basePath + "/" + saveDir + "/" + image.getName() %>" 
	  						alt="<%= image.getName() %>" />
	  				</div>
  				<% } %>
  				
    			<table class="table">
						<thead>
							<tr>
								<th data-field="title" data-align="center" data-sortable="true">№</th>
								<th data-field="teacher" data-align="center" data-sortable="true">
									<span class="translate" data-lang-key="Answer"></span>
								</th>
							</tr>
						</thead>
						<tbody>
						<% int j = 1; %>
						<% for (AnswerBean answer : question.getAnswers()) { %>
							<tr>
								<% if (answer.isCorrect()) { %>
									<td class="success"><%= j %></td>
									<td class="success">
										<span class="tooltip-element" data-placement="right" data-toggle="tooltip" 
												title="Правильна відповідь"><%= answer.getAnswerText() %></span>
									</td>
								<% } else { %>
									<td class="warning"><%= j %></td>
									<td class="warning"><%= answer.getAnswerText() %></td>
								<% } %>
							</tr>
							<% j++; %>
						<% } %>
						</tbody>
					</table>
  				</div>
			<% } %>
		</div>
	</div>
	
<%@ include file="footer.jsp" %>
