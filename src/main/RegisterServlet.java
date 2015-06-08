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
import dao.UserDAO;
import beans.UserBean;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> errorMessageList;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		}
		else if (!user.getAccess("register")) {
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
		String group = request.getParameter("group").trim();
		String avatarName = "";
		String message = formValidate(userName, password, email);

		HttpSession session = request.getSession(false);

		if (errorMessageList.isEmpty()) {
			UserBean user = null;

			try {
				user = new UserBean(userName, password, email, role, firstName, lastName, avatarName, group);
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
			session.setAttribute("message", message);
			response.sendRedirect("register");
		}
	}

	private String formValidate(String name, String pass, String email) {
		
		errorMessageList = new ArrayList<>();
		String errorMessage = "";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		String findName = "SELECT * FROM users "
				+ "WHERE username = ?";
		String findEmail = "SELECT * FROM users "
				+ "WHERE email = ?";
		
		try (PreparedStatement stmt = con.prepareStatement(findName)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				errorMessageList.add("The Username is already used");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try (PreparedStatement stmt = con.prepareStatement(findEmail)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				errorMessageList.add("The email is already used");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		//Email validator
		
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches() == false) {
			errorMessageList.add("Email is not valid.");
		}
		
		
		//Name and password validator
		
		if (name.isEmpty() || pass.isEmpty()) {
			errorMessageList.add("Name or password is Empty");
		}
		
		for(String message: errorMessageList) {
			errorMessage += message + "<br>";
		}
		
		
		return errorMessage; 
	}

}
