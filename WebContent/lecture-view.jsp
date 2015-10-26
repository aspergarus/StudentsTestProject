<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="beans.UserBean"%>
<%@page import="beans.FileBean"%>
<%@page import="beans.LecturesBean"%>
<%@page import="beans.CommentsBean"%>
<%@page import="dao.SubjectsDAO"%>
<%@page import="dao.FileDAO"%>
<%@page import="dao.UserDAO"%>
<%@page import="dao.CommentsDAO"%>
<%@page import="util.FileUploadManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<% String basePath = request.getContextPath(); %>
<% String status = (String) request.getAttribute("status"); %>
<% String message = (String) request.getAttribute("message"); %>
<% String saveDir = (String) request.getAttribute("saveDir"); %>
<% LecturesBean lBean = (LecturesBean) request.getAttribute("lecturesBean"); %>
<% String body = lBean.getBody(); %>
<% ArrayList<FileBean> fileBeans = FileDAO.findAll(lBean.getId(), "lectures"); %>
<% Map<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap(); %>
<% ArrayList<Object[]> comments = CommentsDAO.findByOwner(lBean.getId(), "lectures"); %>
<% UserBean user = (UserBean) session.getAttribute("user"); %>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<div class="container">
	<%	if (status != null && message != null) { %>
		<div class="alert alert-${status}">
			<p>${message}</p>
		</div>
	<% } %>
	<p class="go-back"><a href="<%= basePath %>/lectures">back to lectures</a></p>
	<h1 class="lead"><%= subjectsMap.get(lBean.getSubjectId()) %></h1>
	<h2><%= lBean.getTitle() %></h2>
	<% if (!body.isEmpty()) { %>
		<div class="body"><%= body %></div>
	<% } %>
	<% for (FileBean fileBean : fileBeans) { %>
		<div class="file">
			<a href="<%= saveDir + File.separator + fileBean.getName() %>" download>
			<img src="imgs/icons/<%= FileUploadManager.extractFileExt(fileBean.getName()) %>.png" alt="<%= fileBean.getName() %>" />
			<%= fileBean.getName() %>
			</a>
		</div>
	<% } %>

	<% if (!comments.isEmpty()) { %>
		<div class="comment-list clearfix" id="comments">
		<h2><span class="translate" data-lang-key="Comments"></span></h2>
			<ol>
				<% for(Object[] pack : comments) { %>
					<% UserBean commentAuthor = (UserBean) pack[0]; %>
					<% CommentsBean comment = (CommentsBean) pack[1]; %>
					<li class="comment <%= commentAuthor.getId() == user.getId() ? "comment-editable" : user.getRole() == 2 ? "comment-editable" : "" %>"
						data-author="<%= commentAuthor.getId() %>" data-comment-id="<%= comment.getCid() %>">
						<div class='comment-config'>
							<span class='glyphicon glyphicon-cog'></span>
							<span class='glyphicon glyphicon-remove'></span>
						</div>
						<div class="comment-top"><span></span></div>
						<div class="comment-body">
							<div class="comment-avatar">
								<div class="avatar">
									<%  if (commentAuthor.getAvatar().isEmpty()) { %>
										<img src="<%= defaultAvatar %>" class="img-circle avatar-table">
									<% } else { %>
										<img src="<%= uploadAvatarPath + commentAuthor.getAvatar() %>" class="img-circle avatar-table">
									<% } %>
								</div>
							</div>
							<div class="comment-text">
								<div class="comment-author clearfix">
									<p class="link-author"><%= commentAuthor.getReadableName() %></p>
									<span class="comment-date">
										<% SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm"); %>
										<%= dateFormat.format(comment.getDate()) %>
									</span>
								</div>
								<div class="comment-entry">
									<h4><div class="comment-title"><%= comment.getTitle() %></div></h4>
									<div class="comment-msg"><%= comment.getBody() %></div>
								</div>
							</div>
						</div>
						<div class="clear"></div>
					</li>
				<% } %>
			</ol>
		</div>
	<% } %>

	<div class="comment-form">
		<h2 class="lead"><span class="translate" data-lang-key="Leave your comment"></span></h2>
		<form class="form" method="post" action="<%= basePath %>/comments">
			<div class="form-group">
				<input class="form-control" type="text" name="title" id="title" required placeholder="Comment title...">
			</div>
			<div class="form-group">
				<textarea class="form-control" rows="7" cols="150" name="body" id="body" required placeholder="Comment message..."></textarea>
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary btn-lg">
					<span class="translate" data-lang-key="Send"></span>
				</button>
			</div>
			<input name="ownerId" type="hidden" value="<%= lBean.getId() %>" />
			<input name="author" type="hidden" value="<%= user.getId() %>" />
			<input name="ownerType" type="hidden" value="lectures" />
		</form>
	</div>
</div>

<%@ include file="footer.jsp" %>
