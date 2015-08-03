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
import dao.SubjectsDAO;
import dao.TestsDAO;
import dao.UserDAO;
import beans.TestBean;
import beans.UserBean;

/**
 * Servlet implementation class TestsServlet
 */
@WebServlet("/tests")
public class TestsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestsServlet() {
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
		}
		else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");
			if (status != null && message != null) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			
			// Show exists tests
			HashMap<String, ArrayList<TestBean>> testsMap = TestsDAO.findAll(user);
			HashMap<Integer, String> subjects = SubjectsDAO.getSubjectsMap();
			HashMap<Integer, String> teachers = UserDAO.getTeachersMap();
			HashMap<String, String> groupsMap = GroupsDAO.getGroupsByTeacher(user.getId());
			
			request.setAttribute("subjects", subjects);
			request.setAttribute("teachers", teachers);
			request.setAttribute("currentUser", user);
			request.setAttribute("testsMap", testsMap);
			request.setAttribute("groupsMap", groupsMap);
			
			request.getRequestDispatcher("tests.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendError(403);
		}
		
		else {
			
			// Add new test
			String subject = request.getParameter("subject");
			int subjectId = SubjectsDAO.find(subject);
			byte module = Byte.valueOf(request.getParameter("module"));
			String note = request.getParameter("note");
			int id;
			
			if (user.getRole() == 2) {
				String name = request.getParameter("teacher");
				id = Integer.parseInt(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
			}
			else {
				id = user.getId();
			}
			
			TestBean newTest = new TestBean(id, subjectId, module, note);
			
			if (TestsDAO.insert(newTest)) {
				session.setAttribute("status", "success");
				session.setAttribute("message", "Test has been added successfully");
			}
			else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", "Some troubles were occurred during addition a test");
			}
			response.sendRedirect(request.getContextPath() + "/tests");
			return;
		}
	}
}
