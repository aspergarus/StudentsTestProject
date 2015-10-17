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
		} else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");
			
			if (status != null && message != null) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			
			String id = request.getParameter("id");
			boolean edit = request.getParameter("edit") == null ? false : Boolean.valueOf(request.getParameter("edit"));
			HashMap<Integer, String> departmentsMap = DepartmentsDAO.getDepartmentsMap();
			
			if (edit) {
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
			}
			//Show all subjects
			else {
				ArrayList<SubjectsBean> subjectsList = SubjectsDAO.findAll(user);
				request.setAttribute("subjectsList", subjectsList);
				request.setAttribute("departmentsMap", departmentsMap);
				request.getRequestDispatcher("subjects.jsp").forward(request, response);
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
			String department = request.getParameter("departmentName").trim();
			int departmentId = DepartmentsDAO.find(department).getId();
			
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
				session.setAttribute("status", "danger");
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
			
			int subjectId = Integer.parseInt(request.getHeader("id"));
			String subjectHeader = request.getHeader("subject");
			String departmentHeader = request.getHeader("department");
			
			if (subjectHeader != null && departmentHeader != null) {
				try {
					
					String subjectName = java.net.URLDecoder.decode(subjectHeader, "UTF-8").trim();
					String department = java.net.URLDecoder.decode(departmentHeader, "UTF-8").trim();
					
					int departmentId = DepartmentsDAO.find(department).getId();
					String errorMessage = SubjectsDAO.subjectValidate(subjectName, departmentId);

					if (errorMessage == null) {
						SubjectsBean sBean = new SubjectsBean(subjectId, subjectName, departmentId);
						
						if (SubjectsDAO.update(sBean)) {
							response.getOutputStream().println("Subject has been updated successfully.");
						} else {
							response.getOutputStream().println("Some troubles were occurred during updating a subject.");
						}
					} else {
						response.getOutputStream().println(errorMessage);
					}
					
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
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
