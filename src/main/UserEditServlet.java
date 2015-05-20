package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.ConnectionManager;
import beans.UserBean;
import dao.UserDAO;

/**
 * Servlet implementation class UserEditServlet
 */
@WebServlet("/user/*")
public class UserEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> errorMessageList;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		System.out.println(request.getPathInfo());
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		}
		else if (!user.getAccess("users")) {
			request.setAttribute("status", "warning");
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

			String[] pathParts = request.getPathInfo().split("/");
			int id = Integer.valueOf(pathParts[1]);
			UserBean editedUser = null;
			try {
				editedUser = UserDAO.find(id);
			} catch (SQLException e) {
				System.out.println("Can't find such user.");
				System.out.println(e.getMessage());
			}
			if (editedUser != null) {
				request.setAttribute("editedUser", editedUser);
				request.setAttribute("userRole", user.getRole());
				request.getRequestDispatcher("/formUser.jsp").forward(request, response);
			}
			else {
				response.sendError(404);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("editedusername").trim();
		String password = request.getParameter("editedpass").trim();
		String email = request.getParameter("editedemail").trim();
		byte role = Byte.valueOf(request.getParameter("role"));
		String firstName = request.getParameter("firstname").trim();
		String lastName = request.getParameter("lastname").trim();

		String[] pathParts = request.getPathInfo().split("/");
		System.out.println(request.getPathInfo());
		int i = 0;
		for (String part : pathParts) {
			System.out.println(i++ + ": " + part);
		}
		int id = Integer.valueOf(pathParts[1]);

		String message = formValidate(id, userName, email);

		HttpSession session = request.getSession(false);

		if (errorMessageList.isEmpty()) {
			UserBean user = null;

			try {
				user = UserDAO.find(id);
			    if (UserDAO.update(user, userName, password, email, role, firstName, lastName)) {
			    	session.setAttribute("status", "success");
					session.setAttribute("message", "User '" + userName + "' was updated successfully");
			    }
			    else {
			    	session.setAttribute("status", "danger");
					session.setAttribute("message", "Some problem appears during updating the user.");
			    }
			    response.sendRedirect(request.getContextPath() + "/user/" + id);
			} catch (Throwable theException) {
			     System.out.println(theException);
			}
		}
		else {
			session.setAttribute("status", "danger");
			session.setAttribute("message", message);
			response.sendRedirect(request.getContextPath() + "/user/" + id);
		}
	}

	private String formValidate(int id, String name, String email) {
		errorMessageList = new ArrayList<>();
		String errorMessage = "";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		String findId = "SELECT * FROM users "
				+ "WHERE id = ?";

		try (PreparedStatement stmt = con.prepareStatement(findId)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				errorMessageList.add("This user is not existed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Email validator
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches() == false) {
			errorMessageList.add("Email is not valid.");
		}

		//Name and password validator
		if (name.isEmpty()) {
			errorMessageList.add("Name is wmpty");
		}

		for(String message: errorMessageList) {
			errorMessage += message + "<br>";
		}

		return errorMessage; 
	}

}
