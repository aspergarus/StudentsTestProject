package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import dao.UserDAO;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user != null) {
			response.sendRedirect(request.getContextPath() + "/");
		}
		else {
			String error = null;
			if (session != null) {
				error = (String) session.getAttribute("error");
				session.setAttribute("error", null);
			}
			request.setAttribute("error", error);

			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("login").trim();
		String password = request.getParameter("pass").trim();

		try {
			UserBean user = new UserBean(name);
		    user = UserDAO.find(user);

		    if (user.isValid() && user.isPasswordValid(password)) {
		    	HttpSession session = request.getSession(true);
		    	session.setAttribute("user", user);
		    	response.sendRedirect(request.getContextPath() + "/");
		    }
		    else {
		    	HttpSession session = request.getSession();
		    	if (session != null) {
		    		session.setAttribute("error", "You entered invalid data. Please, try again");
		    	}
		    	response.sendRedirect(request.getContextPath() + "/login");
		    }
		} catch (Throwable theException) {
		     System.out.println(theException);
		}
	}

}
