package main;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DepartmentsDAO;
import beans.DepartmentBean;
import beans.UserBean;

/**
 * Servlet implementation class DepartmentServlet
 */
@WebServlet("/department")
public class DepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DepartmentServlet() {
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
			ArrayList<DepartmentBean> departments = DepartmentsDAO.findAll();
			request.setAttribute("departmentsList", departments);
			request.getRequestDispatcher("departments.jsp").forward(request, response);
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
			// Add new department
			String departmentName = request.getParameter("departmentName");
			String errorMessage = DepartmentsDAO.departmentValidate(departmentName);
			
			if (errorMessage == null) {
				if (DepartmentsDAO.insert(departmentName)) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Department has been added");
				} else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during adding a department");
				}
			} else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", errorMessage);
			}
			response.sendRedirect(request.getContextPath() + "/department");
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (user == null) {
			response.sendError(403);
		} else {
			//Update in DB
			int id = Integer.valueOf(request.getHeader("id"));
			String newDepartmentName = java.net.URLDecoder.decode(request.getHeader("name"), "UTF-8");
			DepartmentBean department = new DepartmentBean(id, newDepartmentName);
			
			if (DepartmentsDAO.update(department)) {
				response.getOutputStream().println("Department has been updated successfully.");
			} else {
				response.getOutputStream().println("Some troubles were occurred during updating a department.");
			}
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (user == null) {
			response.sendError(403);
		} else {
			//Delete from DB
			int id = Integer.valueOf(request.getHeader("id"));
			DepartmentsDAO.delete(id);
			response.getOutputStream().println("Department has been deleted successfully.");
		}
	}

}
