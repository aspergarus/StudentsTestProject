package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.FileUploadManager;
import dao.AnswersDAO;
import dao.FileDAO;
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
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
				maxFileSize=1024*1024*10,      // 10MB
				maxRequestSize=1024*1024*50)   // 50MB
public class TestEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String saveDir = "files" + File.separator + "testQuestionsFiles";
       
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
		} else if (user.getRole() == 0) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
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
				int id = Integer.valueOf(pathParts[1]);
				TestBean editedTest = TestsDAO.find(id);
				ArrayList<QuestionBean> questions = QuestionDAO.getQuestions(editedTest);
				
				request.setAttribute("editedTest", editedTest);
				request.setAttribute("saveDir", saveDir);
				request.setAttribute("questions", questions);
				request.getRequestDispatcher("/test-edit.jsp").forward(request, response);
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
			response.sendRedirect(request.getContextPath() + "/login");
		} else if (user.getRole() == 0) {
			request.setAttribute("status", "warning");
			request.setAttribute("message", "You don't have access to this page.");
			request.getRequestDispatcher(request.getContextPath() + "/error-access.jsp").forward(request, response);
		} else {
			String deleteQuestionId = request.getParameter("delete-question-id");
			
			// Deleting question
			if (deleteQuestionId != null) {
				try {
					int questionId = Integer.parseInt(deleteQuestionId);
					
					if (QuestionDAO.delete(questionId)) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Question was deleted successfully!");
					} else {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Some troubles during deleting question.");
					}
				} catch (Exception e) {
					response.sendRedirect(request.getRequestURI());
					return;
				}
			} else {
				// Adding new question
				String sTestId = request.getParameter("id");
				if (sTestId != null) {
					int testId = Integer.parseInt(sTestId);
					String questionText = request.getParameter("question");
					
					// Upload additional files.
					String filePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
					ArrayList<String> fileNames = FileUploadManager.uploadFiles("upload", filePath, request.getParts());
					
					int answersCount = Integer.valueOf(request.getParameter("count"));
					ArrayList<AnswerBean> answers = new ArrayList<>();
					
					QuestionBean question = new QuestionBean(testId, questionText);
					int questionId = QuestionDAO.add(question);
					
					if (questionId == 0) {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Some troubles during adding a question.");
						response.sendRedirect(request.getRequestURI());
						return;
					}
					question.setId(questionId);
					for (int i = 1; i <= answersCount; i++) {
						String answerText = request.getParameter("answer-" + i);
						String correctAnswer = request.getParameter("correct-answer-" + i);
						boolean isCorrect = (correctAnswer != null) ? true : false;
						AnswerBean answer = new AnswerBean(questionId, answerText, isCorrect);
						answers.add(answer);
					}
					if (AnswersDAO.add(answers)) {
						if (fileNames.isEmpty()) {
							session.setAttribute("status", "success");
							session.setAttribute("message", "Question and answers was added successfully!");
						} else {
							if (FileDAO.insert(question.getId(), "questions", fileNames)) {
								session.setAttribute("status", "success");
								session.setAttribute("message", "Question and answers was added successfully!");
							} else {
								session.setAttribute("status", "danger");
								session.setAttribute("message", "Some troubles were occurred during writing file info to db");
							}
						}
					} else {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Question was added, but we have some troubles during adding answers.");
					}
				} else {
					session.setAttribute("status", "warning");
					session.setAttribute("message", "Some troubles during adding the question.");
				}
			}
			response.sendRedirect(request.getRequestURI());
		}
	}
}
