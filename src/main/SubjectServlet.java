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

import dao.DepartmentsDAO;
import dao.SubjectsDAO;
import beans.DepartmentBean;
import beans.SubjectsBean;
import beans.UserBean;

/**
 * Servlet implementation class SubjectServlet
 */
@WebServlet("/subjects")
public class SubjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubjectServlet() {
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
		} else if (user.getRole() == 0) {
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
			try {
				String id = request.getParameter("id");
				boolean edit = request.getParameter("edit") == null ? false : Boolean.valueOf(request.getParameter("edit"));
				HashMap<Integer, String> departmentsMap = DepartmentsDAO.getDepartmentsMap();
				
				if (edit && user.getRole() == 2) {
					SubjectsBean subjectsBean = SubjectsDAO.find(Integer.valueOf(id));
					if (subjectsBean == null) {
						session.setAttribute("status", "Warning");
						session.setAttribute("message", "Such subject does not exist");
						response.sendRedirect(request.getContextPath() + "/subjects");
					} else {
						request.setAttribute("subjectsBean", subjectsBean);
						request.setAttribute("departmentsMap", departmentsMap);
						request.getRequestDispatcher("subject-edit.jsp").forward(request, response);
					}
				} else {
					//Show all subjects
					ArrayList<SubjectsBean> subjectsList = SubjectsDAO.findAll(user);
					request.setAttribute("subjectsList", subjectsList);
					request.setAttribute("departmentsMap", departmentsMap);
					request.getRequestDispatcher("subjects.jsp").forward(request, response);
				}
			} catch (NumberFormatException e) {
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
		
		if (user == null) {
			response.sendError(403);
		} else {
			String subjectName = request.getParameter("subjectName").trim();
			String departmentName = request.getParameter("departmentName").trim();
			int departmentId = 0;
			
			DepartmentBean department = DepartmentsDAO.find(departmentName);
			if (department != null) {
				departmentId = department.getId();
			} else {
				session.setAttribute("status", "warning");
				session.setAttribute("message", "This department doesn't exist.");
				response.sendRedirect(request.getContextPath() + "/subjects");
				return;
			}
			
			String errorMessage = SubjectsDAO.subjectValidate(subjectName, departmentId);
			if (errorMessage == null) {
				SubjectsBean bean = new SubjectsBean(subjectName, departmentId);
				
				if (SubjectsDAO.insert(bean)) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Subject has been added");
				} else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during adding a subject");
				}
			} else {
				session.setAttribute("status", "warning");
				session.setAttribute("message", errorMessage);
			}
			response.sendRedirect(request.getContextPath() + "/subjects");
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendError(403);
		} else {
			int subjectId = Integer.valueOf(request.getHeader("id"));
			String newSubjectName = java.net.URLDecoder.decode(request.getHeader("data"), "UTF-8");
			
			int departmentId = SubjectsDAO.findDepartment(subjectId);
			String errorMessage = SubjectsDAO.subjectValidate(newSubjectName, departmentId);

			if (errorMessage == null) {
				SubjectsBean sBean = new SubjectsBean(subjectId, newSubjectName, departmentId);
				
				if (SubjectsDAO.update(sBean)) {
					response.getOutputStream().println("Subject has been updated successfully.");
				} else {
					response.getOutputStream().println("Some troubles were occurred during updating a subject.");
				}
			} else {
				response.getOutputStream().println(errorMessage);
			}
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendError(403);
		} else {
			// Delete subject
			String deleteId = request.getHeader("id");
			
			if (deleteId != null) {
				int id = Integer.parseInt(deleteId);
				
				if (SubjectsDAO.delete(id)) {
					response.getOutputStream().println("Subject has been deleted successfully.");
				} else {
					response.getOutputStream().println("Some troubles were occured during deleting a subject.");
				}
			}
		}
	}
}
