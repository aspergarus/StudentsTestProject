package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		if (user == null || user.getRole() != 2) {
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher("error-access.jsp").forward(request, response);
		}
		else {
			String status = null;
			String message = null;
			if (session != null) {
				status = (String) session.getAttribute("status");
				message = (String) session.getAttribute("message");
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			request.setAttribute("status", status);
			request.setAttribute("message", message);
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

		boolean formIsValid = formValidate(request, response, userName, password, email);

		HttpSession session = request.getSession(false);

		if (formIsValid) {
			UserBean user = null;

			try {
				user = new UserBean(userName, password, email, role, firstName, lastName);
			    user = UserDAO.register(user);
			} catch (Throwable theException) {
			     System.out.println(theException);
			}
			finally {
				if (user.isValid()) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "User '" + userName + "' was created successfully");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some problem appears during creating new user.");
				}
				
				response.sendRedirect("register");
			}
		}
		else {
			session.setAttribute("status", "danger");
			session.setAttribute("message", "Required field should not be empty");
			response.sendRedirect("register");
		}
	}

	private boolean formValidate(HttpServletRequest request, HttpServletResponse response, String name, String pass, String email) {
		if (name.isEmpty() || pass.isEmpty() || email.isEmpty()) {
			return false;
		}
		return true;
	}

}
