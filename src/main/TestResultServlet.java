package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;

/**
 * Servlet implementation class TestResultServlet
 */
@WebServlet("/result")
public class TestResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestResultServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");
			int result = session.getAttribute("result") != null ? (int) session.getAttribute("result") : -1;
			
			if (status != null && message != null && result > -1) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				request.setAttribute("result", result);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
				session.setAttribute("result", null);
				session.setAttribute("questions", null);
				request.getRequestDispatcher("test-result.jsp").forward(request, response);
			} else {
				request.setAttribute("status", "warning");
				request.setAttribute("message", "You can see the result after completing the test.");
				request.getRequestDispatcher("error-access.jsp").forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
