package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.FileUploadManager;
import dao.FileDAO;
import beans.FileBean;
import beans.UserBean;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/files")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;

		if (user == null) {
			response.sendError(403);
			
		} else {
			// Find file.
			int fid = Integer.valueOf(request.getHeader("fid"));
			FileBean bean = FileDAO.find(fid);

			if (bean == null) {
				response.sendError(404, "Can't find such file");
				return;
			}

			// Delete file from filesystem.
			String fileType;
			if (bean.getType().equals("lectures")) {
				fileType = "lecturesFiles";
			}
			else {
				fileType = "practicalFiles";
			}
			String path = request.getServletContext().getRealPath("") + File.separator + "files" + File.separator + fileType;
			String fullFilePath = path + File.separator + bean.getName();
			FileUploadManager.delete(fullFilePath);

			// Delete file info from db.
			FileDAO.delete(bean.getFid());

			// Send response.
			response.getOutputStream().println("File has been deleted successfully.");
		}
	}

}
