package main;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dao.DepartmentsDAO;
import dao.GroupsDAO;
import dao.SubjectsDAO;
import dao.UserDAO;
import beans.UserBean;

/**
 * Servlet implementation class AutocompleteServlet
 */
@WebServlet("/autocomplete/*")
public class AutocompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null || !user.getAccess("students")) {
			response.sendError(403);
		}
		else {
			String query = request.getParameter("query");
			if (query == null) {
				query = request.getParameter("term");
			}

			String output = getAutocompleteData(request.getPathInfo(), query);

			response.getWriter().println(output);
		}
	}

	private String getAutocompleteData(String pathInfo, String query) {
		Gson gson = new Gson();
		ArrayList<String> list = new ArrayList<>();
		String out = "";

		switch (pathInfo) {
			case "/students":
				list = UserDAO.findStudents(query);
				out = gson.toJson(list);
				break;
			
			case "/subjects":
				list = SubjectsDAO.findSubjects(query);
				out = gson.toJson(list);
				break;

			case "/group":
				list = UserDAO.findStudentGroups(query);
				out = gson.toJson(list);
				break;
				
			case "/groups":
				list = GroupsDAO.findGroups(query);
				out = gson.toJson(list);
				break;

			case "/departments":
				list = DepartmentsDAO.findAllByName(query);
				out = gson.toJson(list);
				break;
			
			case "/teachers":
				list = UserDAO.findTeachers(query);
				out = gson.toJson(list);
				break;
		}

		return out;
	}

}
