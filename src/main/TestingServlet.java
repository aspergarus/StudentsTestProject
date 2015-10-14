package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.QuestionDAO;
import dao.TestsDAO;
import beans.QuestionBean;
import beans.QuestionAnswersBean;
import beans.UserBean;

/**
 * Servlet implementation class TestingServlet
 */
@WebServlet("/testing/*")
public class TestingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			String status = null;
			String message = null;
			if (session != null) {
				status = (String) session.getAttribute("status");
				message = (String) session.getAttribute("message");
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}
			request.setAttribute("status", status);
			request.setAttribute("message", message);
			
			// Get test id from path.
			String[] pathParts = request.getPathInfo().split("/");
			int testId = Integer.valueOf(pathParts[1]);
			
			if (!TestsDAO.canTesting(user, testId)) {
				request.setAttribute("status", "warning");
				request.setAttribute("message", "You don't have access to this test.");
				request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
			} else {
				ArrayList<QuestionBean> questions = QuestionDAO.getQuestions(testId);
				request.setAttribute("status", "success");
				request.setAttribute("testId", testId);
				request.setAttribute("questions", questions);
				request.getRequestDispatcher("/testing.jsp").forward(request, response);
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
			int testId = Integer.parseInt(request.getParameter("test-id"));
			int questionsNumber = Integer.parseInt(request.getParameter("questions-number"));
			List<QuestionAnswersBean> questionAnswers = new ArrayList<>();
			
			for (int i = 0; i < questionsNumber; i++) {
				String sQuestionId = request.getParameter("question-id" + i);
				String sAnswerId = request.getParameter("answer-to-question" + i);
				
				if (sQuestionId != null && sAnswerId != null) {
					int questionId = Integer.parseInt(sQuestionId);
					int answerId = Integer.parseInt(sAnswerId);
					QuestionAnswersBean questionAnswer = new QuestionAnswersBean(questionId, answerId);
					questionAnswers.add(questionAnswer);
				}
			}
			int result = 0;
			if (questionAnswers.size() > 0) {
				result = TestsDAO.getResult(questionAnswers);
				long completed = System.currentTimeMillis();
				
				if (user.getRole() == 0) {
					if (!TestsDAO.saveResult(user, testId, result, completed)) {
						session.setAttribute("message", "Some troubles occured during saving the test result.");
						session.setAttribute("status", "warning");
					} else {
						session.setAttribute("message", "Test completed successfully. Result saved.");
						session.setAttribute("status", "success");
						TestsDAO.closeTest(testId, user);
					}
				} else {
					session.setAttribute("message", "Test completed successfully.");
					session.setAttribute("status", "success");
				}
				session.setAttribute("result", result);
				response.sendRedirect(request.getContextPath() + "/result");
			}
		}
	}
}
