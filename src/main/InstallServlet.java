package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		HttpSession session = request.getSession(false);
		
		String status = (String) session.getAttribute("status");
		String message = (String) session.getAttribute("message");

		if (status != null && message != null) {
			request.setAttribute("status", status);
			request.setAttribute("message", message);
			session.setAttribute("status", null);
			session.setAttribute("message", null);
		}
		// Status: 0 - all right, N - number of missing tables, -1 - no connection
		int statusDB = InstallConnect.testConnect();
		
		if (statusDB == 0) {
			response.sendRedirect(request.getContextPath() + "/");
		} else {
			request.setAttribute("statusDB", statusDB);
			request.getRequestDispatcher("install-db.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String host = request.getParameter("host");
		String port = request.getParameter("port");
		String dataBaseName = request.getParameter("dataBaseName");
		String admin = request.getParameter("admin");
		String password = request.getParameter("password");
		
		if (InstallConnect.insertSettings(host, port, dataBaseName, admin, password)) {
			if (InstallConnect.addAdmin()) {
				session.setAttribute("status", "success");
				session.setAttribute("message", "Setting was applied. User admin created.");
			}
			response.sendRedirect(request.getContextPath() + "/install");
		}
	}
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		InstallConnect.createTables();
		
		if (InstallConnect.addAdmin()) {
			session.setAttribute("status", "success");
			session.setAttribute("message", "Setting was applied. User admin created.");
		}
		response.getOutputStream().println("Success.");
	}
}
