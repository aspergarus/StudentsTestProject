package main;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AnswersDAO;
import dao.QuestionDAO;
import dao.TestsDAO;
import beans.AnswerBean;
import beans.QuestionBean;
import beans.TestBean;
import beans.UserBean;

/**
 * Servlet implementation class TestEditServlet
 */
@WebServlet("/test/*")
public class TestEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestEditServlet() {
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
		}
		else if (user.getRole() == 0) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
		}
		else {
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
			int id = Integer.valueOf(pathParts[1]);
			
			TestBean editedTest = TestsDAO.find(id);
			ArrayList<QuestionBean> questions = QuestionDAO.getQuestions(id);
			
			for (QuestionBean question: questions) {
				System.out.println(question);
			}
			
			
			if (editedTest != null) {
				request.setAttribute("editedTest", editedTest);
				request.getRequestDispatcher("/questions.jsp").forward(request, response);
			}
			else {
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
			response.sendRedirect(request.getContextPath() + "/login");
		}
		else if (user.getRole() == 0) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
		}
		else {
			
			int testId = Integer.valueOf(request.getParameter("id"));
			String questionText = request.getParameter("question");
			
			int answersCount = Integer.valueOf(request.getParameter("count"));
			ArrayList<AnswerBean> answers = new ArrayList<>();
			
			QuestionBean question = new QuestionBean(testId, questionText);
			
			int questionId = QuestionDAO.add(question);
			
			if (questionId == 0) {
				session.setAttribute("status", "danger");
				session.setAttribute("message", "Some troubles during adding a question.");
				response.sendRedirect(request.getRequestURI());
			}
			
			for (int i = 1; i <= answersCount; i++) {
				String answerText = request.getParameter("answer-" + i);
				String correctAnswer = request.getParameter("correct-answer-" + i);
				boolean isCorrect = (correctAnswer != null) ? true : false;
				AnswerBean answer = new AnswerBean(questionId, answerText, isCorrect);
				answers.add(answer);
			}
			
			if (AnswersDAO.add(answers)) {
				session.setAttribute("status", "success");
				session.setAttribute("message", "Question and answers was added successfully!");
			}
			else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", "Question was added, but we have some troubles during adding answers.");
			}
			
			response.sendRedirect(request.getRequestURI());
		}
	}
}
