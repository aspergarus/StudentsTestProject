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

import dao.PracticalsDAO;
import beans.UserBean;

/**
 * Servlet implementation class subjectsServlet
 */
@WebServlet("/subjects")
public class SubjectsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null) {
			response.sendError(403);
		}
		else {
			String query = request.getParameter("query");

			ArrayList<String> subjects = PracticalsDAO.findSubjects(query);

			Gson gson = new Gson();
			String subjString = gson.toJson(subjects);
			System.out.println(subjString);
			response.getWriter().println(subjString);
		}
	}
}
