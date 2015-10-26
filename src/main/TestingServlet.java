package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import beans.TestBean;
import beans.UserBean;

/**
 * Servlet implementation class TestingServlet
 */
@WebServlet("/testing/*")
public class TestingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String saveDir = "files" + File.separator + "testQuestionsFiles";
      
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
			
			// Get test id from path.
			try {
				String[] pathParts = request.getPathInfo().split("/");
				int testId = Integer.valueOf(pathParts[1]);
				
				if (!TestsDAO.canTesting(user, testId)) {
					request.setAttribute("status", "warning");
					request.setAttribute("message", "You don't have access to this test.");
					request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
				} else {
					TestBean test = TestsDAO.find(testId);
					
					ArrayList<QuestionBean> questions = (ArrayList<QuestionBean>) session.getAttribute("questions");
					if (questions == null) {
						questions = QuestionDAO.getTestQuestions(test);
						session.setAttribute("questions", questions);
					}
					request.setAttribute("status", "success");
					request.setAttribute("test", test);
					request.setAttribute("questions", questions);
					request.setAttribute("saveDir", saveDir);
					request.getRequestDispatcher("/testing.jsp").forward(request, response);
				}
			} catch (Exception e) {
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
			int testId = Integer.parseInt(request.getParameter("test-id"));
			int questionsNumber = Integer.parseInt(request.getParameter("questions-number"));
			List<QuestionAnswersBean> questionAnswers = new ArrayList<>();
			
			for (int i = 0; i < questionsNumber; i++) {
				String sQuestionId = request.getParameter("question-id" + i);
				
				String [] sAnswerId = request.getParameterValues("answer-to-question" + i);
				
				if (sQuestionId != null && sAnswerId != null) {
					int questionId = Integer.parseInt(sQuestionId);
					int[] answersId = Arrays.asList(sAnswerId).stream().mapToInt(Integer::parseInt).toArray();
					
					QuestionAnswersBean questionAnswer = new QuestionAnswersBean(questionId, answersId);
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
