package main;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.StudentGroupBean;
import beans.UserBean;
import dao.StudentDAO;

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
		} else if (!user.getAccess("students")) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher("error-access.jsp").forward(request, response);
		} else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");
			
			if (status != null && message != null) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			// Select all students.
			ArrayList<StudentGroupBean> studentList = StudentDAO.findAll(user);
			request.setAttribute("studentList", studentList);
			request.setAttribute("currentUser", user);
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
		} else {
			// Get form values.
			String name = request.getParameter("name");
			String group = request.getParameter("group");
			
			if (name != null && !name.isEmpty()) {
				group = group.isEmpty() ? "" : group;

				String errorMessage = studentValidate(name);
				if (errorMessage == null) {
					// Add student to list only if he is not in other groups.
					int studentId = Integer.parseInt(getStudentIdFromName(name));
					if (StudentDAO.insert(group, studentId)) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Student has been added to list");
					} else {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Some troubles were occurred during creating a student");
					}
				} else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", errorMessage);
				}
			}
			response.sendRedirect("students");
		}
	}

	private String studentValidate(String studentName) {
		int studentId = 0;
		if (studentName.lastIndexOf("[") == -1) {
			return "Can't find this student. Please, search it using autocomplete.";
		} else {
			studentId = Integer.valueOf(getStudentIdFromName(studentName));
		}
		if (studentBelongGroup(studentId)) {
			return "You can't add the same user to different groups";
		}
		return null;
	}

	private boolean studentBelongGroup(int studentId) {
		return StudentDAO.findStudentCount(studentId) > 0;
	}
	
	private String getStudentIdFromName(String studentName) {
		return studentName.substring(studentName.lastIndexOf("[") + 1, studentName.lastIndexOf("]"));
	}

}
