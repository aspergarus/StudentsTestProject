package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.GroupsDAO;
import dao.TestsDAO;
import beans.TestResultBean;
import beans.UserBean;

/**
 * Servlet implementation class TestResultsServlet
 */
@WebServlet("/testResults")
public class TestResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestResultsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else if (user.getRole() == 0) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher("error-access.jsp").forward(request, response);
		} else {
			try {
				String sTestId = request.getParameter("id");
				int testId = Integer.parseInt(sTestId);
				ArrayList<TestResultBean> results = TestsDAO.getResults(testId);
				HashMap<Integer, String> groupsMap = GroupsDAO.getGroupsMap();
				
				request.setAttribute("results", results);
				request.setAttribute("groupsMap", groupsMap);
				request.getRequestDispatcher("/test-results.jsp").forward(request, response);
			} catch (Exception e) {
				response.sendError(404);
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
