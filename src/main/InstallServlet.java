package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.InstallConnect;
import dao.UserDAO;
import beans.UserBean;

/**
 * Servlet implementation class InstallServlet
 */
@WebServlet("/install")
public class InstallServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InstallServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Status: 0 - all right, N - number of missing tables, -1 - no connection
		int statusDB = InstallConnect.testConnect();
		
		if (statusDB == 0) {
			response.sendRedirect(request.getContextPath() + "/");
		}
		else {
			request.setAttribute("statusDB", statusDB);
			request.getRequestDispatcher("install-db.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String host = request.getParameter("host");
		String port = request.getParameter("port");
		String dataBaseName = request.getParameter("dataBaseName");
		String admin = request.getParameter("admin");
		String password = request.getParameter("password");
		
		if (InstallConnect.insertSettings(host, port, dataBaseName, admin, password)) {
			response.sendRedirect(request.getContextPath() + "/install");
		}
	}
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		InstallConnect.createTables();
		
		String username = request.getHeader("name");
		String password = request.getHeader("pass");
		
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			username = java.net.URLDecoder.decode(username, "UTF-8");
			password = java.net.URLDecoder.decode(password, "UTF-8");
			UserBean user = new UserBean();
			user.setUserName(username);
			user.setPassword(password);
			user.setRole((byte) 2);
			user.setEmail("example@gmail.com");
			user.setGroupId(0);
	        try {
	            UserDAO.register(user);
            } catch (Exception e) {
            	System.out.println(e.getMessage());
            }
            
		}
		response.getOutputStream().println("Success.");
	}
}
