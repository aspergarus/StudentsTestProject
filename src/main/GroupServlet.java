package main;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.GroupsDAO;
import dao.SubjectsDAO;
import beans.GroupBean;
import beans.UserBean;

/**
 * Servlet implementation class DepartmentServlet
 */
@WebServlet("/groups")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupServlet() {
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
			ArrayList<GroupBean> groups = GroupsDAO.findAll();
			request.setAttribute("groupsList", groups);
			request.getRequestDispatcher("/groups.jsp").forward(request, response);
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
			String subjectHeader = request.getHeader("subject");
			String groupsHeader = request.getHeader("groups");
			
			if (subjectHeader != null && groupsHeader != null) {
				try {
					String subject = java.net.URLDecoder.decode(subjectHeader, "UTF-8").trim();
					String groups = java.net.URLDecoder.decode(groupsHeader, "UTF-8").trim();

					int subjectId = SubjectsDAO.find(subject);
					GroupsDAO.shareSubject(user.getId(), subjectId, groups);
					response.getOutputStream().println("Subject has been shared successfully.");
				} catch (NullPointerException e) {
					System.out.println(e.getMessage());
				}
			} else {
				// Add new group
				String groupName = request.getParameter("groupName");
				String errorMessage = GroupsDAO.groupValidate(groupName);
				
				if (errorMessage == null) {
					if (GroupsDAO.insert(groupName)) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Group has been added");
					} else {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Some troubles were occurred during adding a group");
					}
				} else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", errorMessage);
				}
				response.sendRedirect(request.getContextPath() + "/groups");
			}
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
			String newGroupName = java.net.URLDecoder.decode(request.getHeader("data"), "UTF-8");
			GroupBean group = new GroupBean(id, newGroupName);
			GroupsDAO.update(group);
			response.getOutputStream().println("Group has been updated successfully.");
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
			GroupsDAO.delete(id);
			response.getOutputStream().println("Group has been deleted successfully.");
		}
	}

}
