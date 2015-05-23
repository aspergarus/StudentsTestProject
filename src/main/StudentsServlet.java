package main;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.UserBean;
import dao.PracticalsDAO;
import dao.StudentDAO;
import dao.UserDAO;

/**
 * Servlet implementation class StudentsServlet
 */
@WebServlet("/students")
public class StudentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		}
		else if (!user.getAccess("students")) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher("error-access.jsp").forward(request, response);
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

			// Select all users here.
			ArrayList<UserBean> studentList = StudentDAO.findAll(user.getId());
			request.setAttribute("studentList", studentList);
			request.getRequestDispatcher("students.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null || !user.getAccess("students")) {
			response.sendError(403);
		}
		else {
			// Get form values.
	        String name = request.getParameter("name");
	        String studentDeleteId = request.getParameter("delete-id");
	        if (name != null && !name.isEmpty()) {
		        int studentId = Integer.valueOf(name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]")));

				// Add student to list
		        if (StudentDAO.insert(user.getId(), studentId)) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Student has been added to list");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during creating a student");
				}
	        }
	        else if (studentDeleteId != null && !studentDeleteId.isEmpty()) {
	        	// Delete student from list
		        if (StudentDAO.delete(user.getId(), Integer.valueOf(studentDeleteId))) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Student has been deleted successfully from your list");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during deleting a student");
				}
	        }
	        
			response.sendRedirect("students");
		}
	}

}
