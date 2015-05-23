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
import com.google.gson.JsonElement;

import dao.PracticalsDAO;
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

			String output = getAutocompleteData(request.getPathInfo(), query);

			response.getWriter().println(output);
		}
	}

	private String getAutocompleteData(String pathInfo, String query) {
		Gson gson = new Gson();
		ArrayList<String> list = new ArrayList<>();

		switch (pathInfo) {
			case "/students":
				list = UserDAO.findStudents(query);
				break;

			case "/subjects":
				list = PracticalsDAO.findSubjects(query);
				break;
		}
		String out = gson.toJson(list);
		return out;
	}

}
