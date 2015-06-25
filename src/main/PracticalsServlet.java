package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.FileUploadManager;
import dao.FileDAO;
import dao.GroupsDAO;
import dao.PracticalsDAO;
import dao.SubjectsDAO;
import beans.FileBean;
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

			String id = request.getParameter("id");
			boolean edit = request.getParameter("edit") == null ? false : Boolean.valueOf(request.getParameter("edit"));

			if (id == null) {
				// Show all practicals
				Map<String, ArrayList<PracticalsBean>> practicalsMap = PracticalsDAO.findAll(user.getId());
				HashMap<String, String> groups = GroupsDAO.getGroupsByTeacher(user.getId());

				request.setAttribute("practicalsMap", practicalsMap);
				request.setAttribute("currentUser", user);
				request.setAttribute("groupsMap", groups);
				request.getRequestDispatcher("practicals.jsp").forward(request, response);
			}
			else {
				// Show practical info, or editing form for practical/.
				String jsp = edit ? "practical-edit.jsp" : "practical-view.jsp";

				// Show specific practical
				PracticalsBean practicalBean = PracticalsDAO.find(Integer.valueOf(id));
				if (practicalBean == null) {
					session.setAttribute("status", "Warning");
					session.setAttribute("message", "Such practical does not exist");
					response.sendRedirect(request.getContextPath() + "/practicals");
				}
				else {
					request.setAttribute("practicalBean", practicalBean);
					request.setAttribute("saveDir", saveDir);
					request.getRequestDispatcher(jsp).forward(request, response);
				}
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
		}
		else if (!user.getAccess("practicals")) {
			response.sendError(403);
		}
		else {
			// Delete practical
			String deleteId = request.getParameter("delete-id");
			if (deleteId != null) {
				int id = Integer.valueOf(deleteId);
				PracticalsBean pBean = PracticalsDAO.find(id);

				// Delete file from file system.
				ArrayList<FileBean> fileBeans = FileDAO.findAll(pBean.getId(), "practicals");
				String savePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
				FileUploadManager.deleteFiles(fileBeans, savePath);
				FileDAO.deleteAll(fileBeans);

				boolean deletedFlag = PracticalsDAO.delete(id);
				if (deletedFlag) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Practical has been deleted successfully");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during deleting a practical");
				}
				response.sendRedirect(request.getContextPath() + "/practicals");
				return;
			}

			// Update existed practical
			String updateId = request.getParameter("update-id");
			if (updateId != null) {
				// Get form values.
				String subject = request.getParameter("subject").trim();
				String title = request.getParameter("title").trim();
				String body = request.getParameter("body").trim();
				
				int subjectId = SubjectsDAO.findSubjectId(subject);

				String errorMessage = practicalValidate(title, subject, 1);

				if (errorMessage == null) {
					PracticalsBean pBean = PracticalsDAO.find(Integer.valueOf(updateId));

					// Upload additional files.
					String filePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
					ArrayList<String> fileNames = FileUploadManager.uploadFiles("upload", filePath, request.getParts());

					// Save uploaded files in DB.
					if (!fileNames.isEmpty()) {
						if (FileDAO.insert(pBean.getId(), "practicals", fileNames)) {
							session.setAttribute("status", "success");
							session.setAttribute("message", "Lecture has been added");
						}
						else {
							session.setAttribute("status", "danger");
							session.setAttribute("message", "Some troubles were occurred during writing file info to db");
						}
					}

					// Update fields in practical bean.
					pBean.setTitle(title);
					pBean.setBody(body);
					pBean.setSubjectId(subjectId);

					if (PracticalsDAO.update(pBean)) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Practical has been updated");
					}
					else {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Some troubles were occurred during updating a practical");
					}
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", errorMessage);
				}

				response.sendRedirect(request.getContextPath() + "/practicals");
				return;
			}

			// Create new practical
			// Save uploaded file, and retrieve his path.
			String filePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
			ArrayList<String> fileNames = FileUploadManager.uploadFiles("upload", filePath, request.getParts());

			// Get form values.
			String subject = request.getParameter("subject").trim();
			String title = request.getParameter("title").trim();
			String body = request.getParameter("body").trim();
			
			int subjectId = SubjectsDAO.findSubjectId(subject);

			String errorMessage = practicalValidate(title, subject, 0);

			if (errorMessage == null) {
				// Create new practicals bean.
				PracticalsBean bean = new PracticalsBean(user.getId(), subjectId, title, body);
				
				if (PracticalsDAO.insert(bean)) {
					if (fileNames.isEmpty()) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Practical has been added");
					}
					else {
						bean = PracticalsDAO.find(subjectId, title);
						if (FileDAO.insert(bean.getId(), "practicals", fileNames)) {
							session.setAttribute("status", "success");
							session.setAttribute("message", "Practical has been added");
						}
						else {
							session.setAttribute("status", "danger");
							session.setAttribute("message", "Some troubles were occurred during writing file info to db");
						}
					}
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

	private boolean practicalBelongSubject(String title, String subjectId, int numExisted) {
		return PracticalsDAO.findPracticalsCountInSubject(title, subjectId) > numExisted;
	}

	private String practicalValidate(String title, String subjectId, int numExisted) {
		if (practicalBelongSubject(title, subjectId, numExisted)) {
			return "Subject should not contain the same practicals";
		}
		return null;
	}

}
