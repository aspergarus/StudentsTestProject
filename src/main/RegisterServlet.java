package main;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DepartmentsDAO;
import dao.GroupsDAO;
import dao.UserDAO;
import beans.UserBean;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else if (!user.getAccess("register")) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher("error-access.jsp").forward(request, response);
		} else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");

			if (status != null && message != null) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			request.setAttribute("currentUser", user);
			request.getRequestDispatcher("register.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username").trim();
		String password = request.getParameter("pass").trim();
		String email = request.getParameter("email").trim();
		byte role = Byte.valueOf(request.getParameter("role"));
		String firstName = request.getParameter("firstname").trim();
		String lastName = request.getParameter("lastname").trim();
		String groupName = request.getParameter("group").trim();
		int groupId = 0;
		
		long registered = System.currentTimeMillis();
		
		if (role == 1) {
			groupId = DepartmentsDAO.find(groupName).getId();
		} else if (role == 0) {
			groupId = GroupsDAO.find(groupName).getId();
		}
		String avatarName = "";
		ArrayList<String> errorMessageList = UserDAO.formValidate(userName, password, email);

		HttpSession session = request.getSession(false);

		if (errorMessageList.isEmpty()) {
			UserBean user = null;
			try {
				user = new UserBean(userName, password, email, role, firstName, lastName, avatarName, groupId, registered);
			    user = UserDAO.register(user);
			} catch (Throwable e) {
			     System.out.println(e);
			} finally {
				if (user.isValid()) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "User '" + userName + "' was created successfully");
				} else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some problem appears during creating new user.");
				}
				response.sendRedirect("register");
			}
		} else {
			StringBuilder sb = new StringBuilder();
			for (String message : errorMessageList) {
				sb.append(message + "<br>");
			}
			String errorMessage = sb.toString();
			
			session.setAttribute("status", "danger");
			session.setAttribute("message", errorMessage);
			response.sendRedirect("register");
		}
	}
}
