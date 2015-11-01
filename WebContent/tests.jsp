<%@page import="beans.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="beans.TestBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% UserBean currentUser = (UserBean) request.getAttribute("currentUser"); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% HashMap<String, ArrayList<TestBean>> tests = (HashMap<String, ArrayList<TestBean>>) request.getAttribute("testsMap"); %>
<% HashMap<Integer, String> teachers = (HashMap<Integer, String>) request.getAttribute("teachers"); %>
<% HashMap<String, String> groups = (HashMap<String, String>) request.getAttribute("groupsMap"); %>

<%@ include file="header.jsp"%>

<%@ include file="menu.jsp"%>
	
<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
		
	<% if (currentUser.getRole() > 0) { %>
		
		<h3 class="lead"><span class="translate" data-lang-key="Add test"></span></h3>

		<form action="<%= basePath %>/tests" class="form" method="post"
			class="form-horizontal">
			<div class="form-group">
				<label for="subject" class="col-sm-2 control-label">
					<span class="translate" data-lang-key="Subject"></span>*
				</label>
				<div class="col-sm-10">
					<input name="subject" type="text" class="form-control typeahead"
						required autocomplete="off" data-autocomplete-url="autocomplete/subjects">
				</div>
			</div>
			
			<% if (currentUser.getRole() == 2) { %>
				<div class="form-group">
					<label for="teacher" class="col-sm-2 control-label">
						<span class="translate" data-lang-key="Teacher"></span>*
					</label>
					<div class="col-sm-10">
						<input name="teacher" type="text" class="form-control typeahead"
							required autocomplete="off" data-autocomplete-url="autocomplete/teachers">
					</div>
				</div>
			<% } %>
			
			<div class="form-group">
				<label for="module" class="col-sm-2 control-label">
					<span class="translate" data-lang-key="Module"></span>*
				</label>
				<div class="col-sm-10">
					<input name="module" type="number" class="form-control" required min="1" max="8"/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="note" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Note"></span>
				</label>
				<div class="col-sm-10">
					<textarea name="note" class="form-control text-area" rows="3"></textarea>
				</div>
			</div>
			
			<div class="form-group">
				<label for="time" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Time"></span>*
					<span class="tooltip-element" data-placement="top" 
						data-toggle="tooltip" title="Час, який виділяється на проходження тесту"><sub>?</sub></span>
				</label>
				
				<div class="col-sm-10">
					<input type="number" name="time" class="form-control" required min="1"/>
				</div>
			</div>
			
			<div class="form-group">
				<label for="test-questions" class="col-sm-2 control-label required">
					<span class="translate" data-lang-key="Number of questions"></span>*
					<span class="tooltip-element" data-placement="top" 
						data-toggle="tooltip" title="Кількість запитань, які будуть вибрані рандомно"><sub>?</sub></span>
				</label>
				<div class="col-sm-10">
					<input type="number" name="test-questions" class="form-control" required min="1"/>
				</div>
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">
						<span class="translate" data-lang-key="Add"></span>
					</button>
				</div>
			</div>
		</form>
	<% } %>
</div>

<div class="container">
	<% if (tests.size() == 0) { %>
		<% if (currentUser.getRole() == 0) { %>
			<h2><span class="translate" data-lang-key="No one test for you now"></span>.</h2>
		<% } else if (currentUser.getRole() == 1) { %>
			<h2><span class="translate" data-lang-key="You don't have any test"></span>.</h2>
		<% } else { %>
			<h2><span class="translate" data-lang-key="There are no any test"></span>.</h2>
		<% } %>
	<% } else { %>
		<h1>
			<span class="translate" data-lang-key="Tests"></span>
			(<span class="item-number"><%= tests.size() %></span>)
		</h1>
		<% if (currentUser.getRole() > 0) { %>
		<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			<% int i = 0; %>
			<% for (String subject : tests.keySet()) { %>
				<% i++; %>
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="heading-<%= i %>">
						<h4 class="panel-title">
							<a class="subject-<%= i %>" data-toggle="collapse" data-parent="#accordion" 
								href="#collapse-<%= i %>" aria-controls="collapse-<%= i %>">
							  <%= subject %>
							</a>
						</h4>
					</div>
		
					<div id="collapse-<%= i %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-<%= i %>">
						<div class="panel-body">
							<% if (currentUser.getRole() == 1) { %>
								<p class="help-block"><span class="translate" data-lang-key="Share this subject to groups"></span>:
									<input type="text" class="tokenfield" 
										value="<%= groups.get(subject) == null ? "" :  groups.get(subject) %>" 
											name="groupName" required autocomplete="off" />
									<input type="submit" value="Share" class="btn btn-info btn-share assign-subject-group" 
										data-num="<%= i %>" data-subject="<%= subject %>" />
								</p>
							<% } %>
							<table class="table" data-unique-id="id">
								<thead>
									<tr>
										<% if (currentUser.getRole() == 2) { %>
											<th data-field="id" data-align="center" data-sortable="true">ID</th>
										<% } %>
										<th data-field="teacher" data-align="center" data-sortable="true">
											<span class="translate" data-lang-key="Teacher"></span>
										</th>
										<th data-field="module" data-align="center" data-sortable="true">
											<span class="translate" data-lang-key="Module"></span>
										</th>
										<% if (currentUser.getRole() == 1) { %>
											<th data-field="note" data-align="center" data-sortable="true">
												<span class="translate" data-lang-key="Note"></span>
											</th>
										<% } %>
										<th data-field="time" data-align="center" data-sortable="true">
											<span class="translate" data-lang-key="Time"></span>
										</th>
										<th data-field="test-questions" data-align="center" data-sortable="true">
											<span class="translate" data-lang-key="Number of questions"></span>
										</th>
										<th data-field="edit" data-align="center">
											<span class="translate" data-lang-key="Edit"></span>
										</th>
										<th data-field="open" data-align="center">
											<span class="translate" data-lang-key="Open"></span>
										</th>
										<th data-field="results" data-align="center">
											<span class="translate" data-lang-key="Results"></span>
										</th>
										<th data-field="start" data-align="center">
											<span class="translate" data-lang-key="Begin test"></span>
										</th>
										<th data-field="delete" data-align="center">
											<span class="translate" data-lang-key="Delete"></span>
										</th>
									</tr>
								</thead>
								<tbody>
								<% for (TestBean test : tests.get(subject)) { %>
									<tr>
										<% if (currentUser.getRole() == 2) { %>
											<td><%= test.getId() %></td>
										<% } %>
										<td><%= teachers.get(test.getTeacherId()) %></td>
										<td>
											<a class="transformer-text" data-path="tests" data-parameter="module" 
												data-id=<%= test.getId() %>><%= test.getModule() %></a>
											<input type="text" style="display: none">
										</td>
										<% if (currentUser.getRole() == 1) { %>
											<td>
												<a class="transformer-text" data-path="tests" data-parameter="note" 
													data-id=<%= test.getId() %>>
													<%= (!test.getNote().equals("")) ? test.getNote() : "-" %>
												</a>
												<input type="text" style="display: none">
											</td>
										<% } %>
										<td>
											<a class="transformer-text" data-path="tests" data-parameter="time" 
												data-id=<%= test.getId() %>>
												<%= test.getTime() / 60 %>
											</a>
											<input type="text" style="display: none">
										</td>
										<td>
											<a class="transformer-text" data-path="tests" data-parameter="test_questions" 
												data-id=<%= test.getId() %>>
												<%= test.getTestQuestions() %>
											</a>
											<input type="text" style="display: none">
										</td>
										<td>
											<a href="test/<%= test.getId() %>">
												<span class="translate" data-lang-key="Edit"></span>
											</a>
										</td>
										<td>
											<a href="openTest?id=<%= test.getId() %>">
												<span class="translate" data-lang-key="Open"></span>
											</a>
										</td>
										<td>
											<a href="testResults?id=<%= test.getId() %>">
												<span class="translate" data-lang-key="Results"></span>
											</a>
										</td>
										<td>
											<a href="testing/<%= test.getId() %>">
												<button class="btn btn-success">
													<span class="translate" data-lang-key="Begin test"></span>
												</button>
											</a>
										</td>	
										<td>
											<button class="btn btn-danger delete-item" data-id="<%= test.getId() %>" 
												data-path="/tests" data-item="test">
											<span class="translate" data-lang-key="Delete"></span>
											</button>
										</td>
									</tr>
								<% } %>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			<% } %>
		</div>
		<% } else { %>
			<div class="container">
	    		<table class="table">
					<thead>
						<tr>
							<th data-field="subject" data-align="center">
								<span class="translate" data-lang-key="Subject"></span>
							</th>
							<th data-field="teacher" data-align="center">
								<span class="translate" data-lang-key="Teacher"></span>
							</th>
							<th data-field="module" data-align="center">
								<span class="translate" data-lang-key="Module"></span>
							</th>
							<th data-field="begin" data-align="center">
								<span class="translate" data-lang-key="Begin test"></span>
							</th>
						</tr>
					</thead>
					<tbody>
						<% for (String subject : tests.keySet()) { %>
							<% for (TestBean test : tests.get(subject)) { %>
								<tr>
									<td><%= subject %></td>
									<td><%= teachers.get(test.getTeacherId()) %></td>
									<td><%= test.getModule() %></td>
									<td>
										<a href="testing/<%= test.getId() %>">
											<button class="btn btn-success">
												<span class="translate" data-lang-key="Begin test"></span>
											</button>
										</a>
									</td>	
								</tr>
							<% } %>
						<% } %>		
					</tbody>
				</table>
			</div>
		<% } %>
	<% } %>
</div>

<%@ include file="footer.jsp"%>
