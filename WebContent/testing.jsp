<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% int testId = (int) request.getAttribute("testId"); %>
<% ArrayList<QuestionBean> questions = (ArrayList<QuestionBean>) request.getAttribute("questions"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
		<% } %>
		<div class="col-md-8 col-md-offset-1">
			<form action="<%= basePath %>/testing/" id="testing-form" method="post" class="form-horizontal" >
				<% int i = 0; %>
				<% for(QuestionBean question : questions) { %>
					<div class="row question-block uncompleted">
		               		<div class="form-group">
			                    <h2 class="question-text"><%= question.getQuestionText() %></h2>
			                   	<input type="hidden" name="question-id<%= i %>" value="<%= question.getId() %>">
		                   	</div>
		                   	<hr>
		                   	<% for(AnswerBean answer : question.getAnswers()) { %>
		                   		<div class="form-group">
		                   			<% if (question.getTrueAnswers() == 1) { %>
					                   	<div class="radio">
											<label class="answer-text">
										    	<input type="radio" class="radio" name="answer-to-question<%= i %>"
										    		value="<%= answer.getAnswerId() %>">
										    	<%= answer.getAnswerText() %>
										  	</label>
										</div>
									<% } else { %>
										<div>
											<label class="answer-text">
										    	<input type="checkbox" name="answer-to-question<%= i %>" value="<%= answer.getAnswerId() %>">
										    	<span class="answer-text"><%= answer.getAnswerText() %></span>
										  	</label>
										</div>
									<% } %>
								</div>
							<% } %>
		            </div>
		            <% i++; %>
	            <% } %>
	            <div class="row test-complete-info hidden">
	               	<div class="form-group">
						<h2 class="question-text">You have given the answers to all questions!</h2>
						<h3><small>For checking click the Complete.</small></h3>
					</div>
					<hr>
				</div>
				
	            <div class="form-group">
					<input type="button" class="btn btn-lg btn-success" id="next-question" value="Next"/>
					<input type="button" class="btn btn-lg btn-info" id="miss-question" value="Miss"/>
				</div>
				<div class="form-group">
					<input type="hidden" name="test-id" value="<%= testId %>"/>
					<input type="hidden" name="questions-number" value="<%= questions.size() %>"/>
					<input type="submit" class="btn btn-primary btn-lg hidden" value="Complete"/>
				</div>
			</form>
		</div>
		<div class="col-md-2">
			<h2>Timer</h2>
		</div>
	</div>
	
<%@ include file="footer.jsp" %>
