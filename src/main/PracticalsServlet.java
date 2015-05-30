package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.PracticalsDAO;
import beans.PracticalsBean;
import beans.UserBean;

/**
 * Servlet implementation class PracticalsServlet
 */
@WebServlet("/practicals")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
				 maxFileSize=1024*1024*10,      // 10MB
				 maxRequestSize=1024*1024*50)   // 50MB
public class PracticalsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String saveDir = "files" + File.separator + "practicalFiles";

	/**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		}
		else {
			String status = (String) session.getAttribute("status");
			String message = (String) session.getAttribute("message");
			if (status != null && message != null) {
				request.setAttribute("status", status);
				request.setAttribute("message", message);
				session.setAttribute("status", null);
				session.setAttribute("message", null);
			}

			Map<String, ArrayList<PracticalsBean>> practicalsMap = PracticalsDAO.findAll(user.getId());

			request.setAttribute("practicalsMap", practicalsMap);
			request.setAttribute("userRole", user.getRole());
			request.setAttribute("currentUser", user);
			request.getRequestDispatcher("practicals.jsp").forward(request, response);
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
		}
		else {
			// Save uploaded file, and retrieve his path.
			String fileName = uploadFile("upload", request);

			// Get form values.
			String subject = request.getParameter("subject").trim();
			String title = request.getParameter("title").trim();
			String body = request.getParameter("body").trim();

			String errorMessage = practicalValidate(title, subject);

			if (errorMessage == null) {
				// Create new practicals bean.
				PracticalsBean bean = new PracticalsBean(user.getId(), subject, title, body, fileName);
				
				if (PracticalsDAO.insert(bean)) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Practical has been added");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during creating a practical");
				}
			}
			else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", errorMessage);
			}

			response.sendRedirect(request.getContextPath() + "/practicals");
		}
	}

	private String uploadFile(String fileFieldName, HttpServletRequest request) {
		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + saveDir;
		String fileName = null;

		try {
			for (Part part : request.getParts()) {
				String name = part.getName();
				if (name.equals(fileFieldName)) {
					fileName = extractFileName(part);
					if (!fileName.isEmpty()) {
						fileName = checkExistingFileName(fileName);
						part.write(savePath + File.separator + fileName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	private String checkExistingFileName(String fileFullName) {
		String fileExt = null, fileName = null;
		if (fileFullName.indexOf(".") > 0) {
			fileExt = fileFullName.substring(fileFullName.indexOf("."));
			fileName = fileFullName.substring(0, fileFullName.indexOf("."));
		}
		else {
			fileExt = "";
			fileName = fileFullName;
		}
		int count = PracticalsDAO.equivalentFileCount(fileName);
		if (count == 0) {
			return fileFullName;
		}
		return fileName + "_" + (count + 1) + fileExt;
	}

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()-1);
			}
		}
		return "";
	}

	private boolean practicalBelongSubject(String title, String subject) {
		return PracticalsDAO.findPracticalsCountInSubject(title, subject) > 0;
	}

	private String practicalValidate(String title, String subject) {
		if (practicalBelongSubject(title, subject)) {
			return "Subject should not contain the same practicals";
		}
		return null;
	}

}
