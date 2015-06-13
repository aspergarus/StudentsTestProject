package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.CommentsBean;
import beans.UserBean;
import dao.CommentsDAO;

/**
 * Servlet implementation class CommentsServlet
 */
@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (user == null) {
			response.sendError(403);
		}
		else {
			// Get info of new comment
			CommentsBean comment = new CommentsBean();
			comment.setTitle(request.getParameter("title"));
			comment.setBody(request.getParameter("body"));
			comment.setOwnerId(Integer.valueOf(request.getParameter("ownerId")));
			comment.setAuthor(Integer.valueOf(request.getParameter("author")));
			comment.setOwnerType(request.getParameter("ownerType"));
			comment.setDate(System.currentTimeMillis());

			// Validate info of new comment
			String errorMessage = commentValidate(comment);

			// Create new comment if everything is fines
			if (errorMessage == null) {
				if (CommentsDAO.insert(comment)) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Comments has been added");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during adding a comment");
				}
			}
			else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", errorMessage);
			}
			response.sendRedirect(request.getContextPath() + "/" + comment.getOwnerType() + "?id=" + comment.getOwnerId());
		}
	}

	/**
	 * Validate whether comments has filled title and body.
	 */
	private String commentValidate(CommentsBean comment) {
		if (comment.getTitle().trim().isEmpty()) {
			return "Comments should have title.";
		}
		if (comment.getBody().trim().isEmpty()) {
			return "Comments should have body.";
		}
		return null;
	}

}
