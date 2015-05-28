<%@page import="beans.UserBean"%>
<% String basePathMenu = request.getContextPath(); %>
<% UserBean currentUserMenu = (UserBean) session.getAttribute("user"); %>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<%= request.getContextPath() %>"><span class="translate" data-lang-key="Students Test Project"></span></a>
		</div>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="nav navbar-nav top-menu">
				<li><a href="<%= basePathMenu %>"><span class="translate" data-lang-key="Home"></span></a></li>
				<% if (currentUserMenu.getRole() == 2) { %>
					<li><a href="<%= basePathMenu %>/users"><span class="translate" data-lang-key="Users"></span></a></li>
					<li><a href="<%= basePathMenu %>/register"><span class="translate" data-lang-key="Register"></span></a></li>
				<% } %>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><p class="navbar-text welcome"><span class="translate" data-lang-key="Welcome"></span></p></li>
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-expanded="false">
						<%= currentUserMenu.getReadableName() %> <span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="<%= basePathMenu %>/user/<%= currentUserMenu.getId() %>"><span class="translate" data-lang-key="Edit profile"></span></a></li>
						<li><a href="<%= basePathMenu %>/logout"><span class="translate" data-lang-key="Logout"></span></a></li>
					</ul>
				</li>
				<li><a href="#" data-lang="en" class="translate-trigger"><img class="change-picture" src="<%= basePathMenu %>/imgs/ua.png"></a></li>
			</ul>
		</div>
	</div>
</nav>
