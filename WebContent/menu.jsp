<%@page import="beans.UserBean"%>
<% String basePathMenu = request.getContextPath(); %>
<% UserBean currentUserMenu = (UserBean) request.getAttribute("currentUser"); %>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<%= request.getContextPath() %>">Students Test Project</a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav top-menu">
				<li><a href="<%= basePathMenu %>">Home</a></li>
				<% if (currentUserMenu.getRole() == 2) { %>
					<li><a href="<%= basePathMenu %>/users">Users</a></li>
					<li><a href="<%= basePathMenu %>/register">Register</a></li>
				<% } %>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><p class="navbar-text welcome">Welcome</p></li>
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-expanded="false">
						<%= currentUserMenu.getReadableName() %> <span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="<%= basePathMenu %>/user/<%= currentUserMenu.getId() %>">Edit profile</a></li>
						<li><a href="<%= basePathMenu %>/logout">Logout</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>
