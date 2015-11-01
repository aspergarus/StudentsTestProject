<%@page import="beans.UserBean"%>
<%@page import="java.io.File"%>

<% String basePathMenu = request.getContextPath(); %>
<% UserBean currentUserMenu = (UserBean) session.getAttribute("user"); %>
<% String defaultAvatar = basePathMenu + "/imgs/user-avatar.png"; %>
<% String uploadAvatarPath = basePathMenu + "/files/avatars/"; %>
<% String avatar = defaultAvatar; %>
<% if (!currentUserMenu.getAvatar().isEmpty()) {
	avatar = uploadAvatarPath + currentUserMenu.getAvatar();
} %>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<%= request.getContextPath() %>"><span class="translate" data-lang-key="Students Project"></span></a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav top-menu">
				<li><a href="<%= basePathMenu %>"><span class="translate" data-lang-key="Home"></span></a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li></li>
				<li class="dropdown">
					<a class="dropdown-toggle dropdown-user" data-toggle="dropdown" href="#" role="button" aria-expanded="false">
					<img src="<%= avatar %>" class="img-circle avatar-menu">
						<%= currentUserMenu.getReadableName() %> <span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="<%= basePathMenu %>/user/<%= currentUserMenu.getId() %>"><span class="translate" data-lang-key="Edit profile"></span></a></li>
						<li><a href="<%= basePathMenu %>/logout"><span class="translate" data-lang-key="Log out"></span></a></li>
					</ul>
				</li>
				<li><a href="#" data-lang="en" class="translate-trigger"><img class="change-picture" src="<%= basePathMenu %>/imgs/ua.png"></a></li>
				<li><p class="navbar-text" id="time"></p></li>
			</ul>
		</div>
	</div>
</nav>
<% if (currentUserMenu.getRole() == 2) { %>
	<div id="show-left-menu-container">
		<a href="#" id="show-left-menu"><span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span></a>
	</div>
	<div id="left-menu">
		<a href="#" id="hide-left-menu"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span></a>
		<ul>
			
				<li><a href="<%= basePathMenu %>/users"><span class="translate" data-lang-key="Users"></span></a></li>
				<li><a href="<%= basePathMenu %>/register"><span class="translate" data-lang-key="Register"></span></a></li>
				<li><a href="<%= basePathMenu %>/departments"><span class="translate" data-lang-key="Departments"></span></a></li>
				<li><a href="<%= basePathMenu %>/subjects"><span class="translate" data-lang-key="Subjects"></span></a></li>
				<li><a href="<%= basePathMenu %>/groups"><span class="translate" data-lang-key="Groups"></span></a></li>
		</ul>
	</div>
<% } %>
