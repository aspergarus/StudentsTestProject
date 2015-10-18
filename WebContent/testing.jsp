<%@page import="beans.QuestionBean"%>
<%@page import="beans.AnswerBean"%>
<%@page import="beans.TestBean"%>
<%@page import="beans.FileBean"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% saveDir = saveDir.replaceAll("\\\\", "/"); %>
<% TestBean test = (TestBean) request.getAttribute("test"); %>
<% ArrayList<QuestionBean> questions = (ArrayList<QuestionBean>) request.getAttribute("questions"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

	<div class="container">

		<%	if (status != null && message != null) { %>
			<div class="alert alert-${status}">
				<p>${message}</p>
			</div>
		<% } %>
		<div class="progress">
  			<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="1" 
  				aria-valuemin="0" aria-valuemax="15" style="width: <%= ((double)1 / questions.size()) * 100 %>%">
    			<p><span id="current-number-question">1</span> / <span id="all-questions"></span></p> 
  			</div>
		</div>
		<div class="col-md-8 col-md-offset-1">
			<form action="<%= basePath %>/testing/" id="testing-form" method="post" class="form-horizontal" >
				<% int i = 0; %>
				<% for(QuestionBean question : questions) { %>
					<div class="row question-block uncompleted">
		               		<div class="form-group">
			                    <h2 class="question-text"><%= question.getQuestionText() %></h2>
			                   	<input type="hidden" name="question-id<%= i %>" value="<%= question.getId() %>">
		                   	</div>
		                   	<% FileBean image = question.getImage(); %>
			  				<% if (image != null) { %>
				  				<img class="question-img" src="<%= basePath + "/" + saveDir + "/" + image.getName() %>" 
				  					alt="<%= image.getName() %>" />
				  				
			  				<% } %>
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
	           		<input type="button" class="btn btn-lg btn-info" id="miss-question" value="Miss"/>
					<input type="button" class="btn btn-lg btn-success" id="next-question" value="Next"/>	
				</div>
				<div class="form-group">
					<input type="hidden" name="test-id" value="<%= test.getId() %>"/>
					<input type="hidden" name="questions-number" value="<%= questions.size() %>"/>
					<input type="submit" class="btn btn-primary btn-lg hidden" value="Complete"/>
				</div>
			</form>
		</div>
		<div class="col-md-3">
			<div class="example" data-timer="<%= test.getTime() %>"></div>
		</div>
	</div>
	<script>
	$(window).on('beforeunload', function(){
		if ($('.uncompleted').length > 0 ) {
			return "Be careful! The test will start over!";
		}
	});
	</script>
	
<%@ include file="footer.jsp" %>
