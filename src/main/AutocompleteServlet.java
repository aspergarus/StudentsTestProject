package main;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ini4j.Ini;

import com.google.gson.Gson;

import dao.PracticalsDAO;
import dao.LecturesDAO;
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
		String out = "";

		switch (pathInfo) {
			case "/students":
				list = UserDAO.findStudents(query);
				out = gson.toJson(list);
				break;

			case "/practicalSubjects":
				list = PracticalsDAO.findSubjects(query);
				out = gson.toJson(list);
				break;
				
			case "/lecturesSubjects":
				list = LecturesDAO.findSubjects(query);
				out = gson.toJson(list);
				break;
		}

		return out;
	}

}
