package main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import config.ConnectionManager;
import beans.UserBean;
import dao.UserDAO;

/**
 * Servlet implementation class UserEditServlet
 */
@WebServlet("/user/*")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*5,      // 5MB
maxRequestSize=1024*1024*10)   // 10MB
public class UserEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> errorMessageList;
	public static final String saveDir = "files" + File.separator + "avatars";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else if (!user.getAccess(request.getServletPath() + request.getPathInfo())) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
		} else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");

			if (status != null && message != null) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			UserBean editedUser = null;

			// Get user id from path.
			try {
				String[] pathParts = request.getPathInfo().split("/");
				int id = Integer.valueOf(pathParts[1]);
				
				editedUser = UserDAO.find(id);
				
				if (editedUser != null) {
					request.setAttribute("editedUser", editedUser);
					request.setAttribute("userRole", user.getRole());
					request.getRequestDispatcher("/formUser.jsp").forward(request, response);
				} else {
					response.sendError(404);
				}
			} catch (Exception e) {
				response.sendError(404);
			}
			
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (user == null || !user.getAccess(request.getServletPath() + request.getPathInfo())) {
			response.sendError(403);
			return;
		}

		String userName = request.getParameter("editedusername").trim();
		String password = request.getParameter("editedpass").trim();
		String email = request.getParameter("editedemail").trim();
		byte role = Byte.valueOf(request.getParameter("role"));
		String firstName = request.getParameter("firstname").trim();
		String lastName = request.getParameter("lastname").trim();

		String[] pathParts = request.getPathInfo().split("/");
		int id = Integer.valueOf(pathParts[1]);
		
		// Save uploaded file, and retrieve his path.
		String avatarName = uploadFile("avatar", request, session);

		String message = formValidate(id, userName, email);

		if (errorMessageList.isEmpty()) {
			UserBean editedUser = null;

			try {
				editedUser = UserDAO.find(id);
				UserBean updatedUser = new UserBean(userName, password, email, role, firstName, lastName, avatarName);
			    if (UserDAO.update(editedUser, updatedUser)) {
			    	session.setAttribute("status", "success");
			    	if (user.getId() == editedUser.getId()) {
			    		session.setAttribute("user", updatedUser);
			    	}
					session.setAttribute("message", "User '" + userName + "' was updated successfully");
			    }
			    else {
			    	session.setAttribute("status", "danger");
					session.setAttribute("message", "Some problem appears during updating the user.");
			    }
			    response.sendRedirect(request.getRequestURI());
			} catch (Throwable theException) {
			     System.out.println(theException);
			}
		}
		else {
			session.setAttribute("status", "danger");
			session.setAttribute("message", message);
			response.sendRedirect(request.getRequestURI());
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
	
	private String uploadFile(String fileFieldName, HttpServletRequest request, HttpSession session) {
		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + saveDir;
		String fileName = null;
		
		UserBean user = (UserBean) session.getAttribute("user");
		if (!user.getAvatar().isEmpty()) {
			File file = new File(savePath + File.separator + user.getAvatar());
			file.delete();
		}
		
		try {
			for (Part part : request.getParts()) {
				String name = part.getName();
				if (name.equals(fileFieldName)) {
					fileName = extractFileName(part);
					if (!fileName.isEmpty()) {
						part.write(savePath + File.separator + fileName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()-1);
			}
		}
		return "";
	}

}
