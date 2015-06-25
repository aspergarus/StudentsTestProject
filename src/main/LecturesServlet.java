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
import beans.FileBean;
import beans.LecturesBean;
import beans.UserBean;
import dao.FileDAO;
import dao.GroupsDAO;
import dao.LecturesDAO;
import dao.SubjectsDAO;

/**
 * Servlet implementation class LecturesServlet
 */
@WebServlet("/lectures")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
				 maxFileSize=1024*1024*10,      // 10MB
				 maxRequestSize=1024*1024*50)   // 50MB
public class LecturesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String saveDir = "files" + File.separator + "lecturesFiles";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LecturesServlet() {
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
				// Show all lectures
				Map<String, ArrayList<LecturesBean>> lecturesMap = LecturesDAO.findAll(user);
				
				HashMap<String, String> groups = GroupsDAO.getGroupsByTeacher(user.getId());
				
				request.setAttribute("lecturesMap", lecturesMap);
				request.setAttribute("currentUser", user);
				request.setAttribute("groupsMap", groups);
				request.getRequestDispatcher("lectures.jsp").forward(request, response);
			}
			else {
				// Show lecture info, or editing form for lecture/.
				String jsp = edit ? "lecture-edit.jsp" : "lecture-view.jsp";

				//Show or edit specific lecture
				LecturesBean lecturesBean = LecturesDAO.find(Integer.valueOf(id));
				if (lecturesBean == null) {
					session.setAttribute("status", "Warning");
					session.setAttribute("message", "Such lecture does not exist");
					response.sendRedirect(request.getContextPath() + "/lectures");
				}
				else {
					request.setAttribute("lecturesBean", lecturesBean);
					request.setAttribute("saveDir", saveDir);
					request.getRequestDispatcher(jsp).forward(request, response);
				}
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		
		if (user == null) {
			response.sendError(403);
			
		} else {
			
			// Delete lecture
			String deleteId = request.getParameter("delete-id");
			if (deleteId != null) {
				int id = Integer.valueOf(deleteId);
				LecturesBean lBean = LecturesDAO.find(id);

				// Delete file from file system and from db.
				ArrayList<FileBean> fileBeans = FileDAO.findAll(lBean.getId(), "lectures");
				String savePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
				FileUploadManager.deleteFiles(fileBeans, savePath);
				FileDAO.deleteAll(fileBeans);

				boolean deletedFlag = LecturesDAO.delete(id);
				if (deletedFlag) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Lecture has been deleted successfully");
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during deleting a lecture");
				}
				response.sendRedirect(request.getContextPath() + "/lectures");
				return;
			}
			
			// Update existed lecture
			String updateId = request.getParameter("update-id");
			if (updateId != null) {
				// Get form values.
				String subject = request.getParameter("subject").trim();
				String title = request.getParameter("title").trim();
				String body = request.getParameter("body").trim();
				
				int subjectId = SubjectsDAO.findSubjectId(subject);

				String errorMessage = lectureValidate(title, subjectId, 1);

				if (errorMessage == null) {
					LecturesBean lBean = LecturesDAO.find(Integer.valueOf(updateId));

					// Upload additional files.
					String filePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
					ArrayList<String> fileNames = FileUploadManager.uploadFiles("upload", filePath, request.getParts());

					// Save uploaded files in DB.
					if (!fileNames.isEmpty()) {
						if (FileDAO.insert(lBean.getId(), "lectures", fileNames)) {
							session.setAttribute("status", "success");
							session.setAttribute("message", "Lecture has been added");
						}
						else {
							session.setAttribute("status", "danger");
							session.setAttribute("message", "Some troubles were occurred during writing file info to db");
						}
					}

					// Update fields in lecture bean.
					lBean.setTitle(title);
					lBean.setBody(body);
					lBean.setSubjectId(subjectId);

					if (LecturesDAO.update(lBean)) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Lecture has been updated");
					}
					else {
						session.setAttribute("status", "danger");
						session.setAttribute("message", "Some troubles were occurred during updating a lecture");
					}
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", errorMessage);
				}

				response.sendRedirect(request.getContextPath() + "/lectures");
				return;
			}
			
			// Create new lecture
			// Save uploaded files, and retrieve their names.
			String filePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
			ArrayList<String> fileNames = FileUploadManager.uploadFiles("upload", filePath, request.getParts());
			
			// Get form values.
			String subject = request.getParameter("subject").trim();
			String title = request.getParameter("title").trim();
			String body = request.getParameter("body").trim();
			
			int subjectId = SubjectsDAO.findSubjectId(subject);
			
			String errorMessage = lectureValidate(title, subjectId, 0);
			
			if (errorMessage == null) {
				// Create new lectures bean.
				LecturesBean bean = new LecturesBean(user.getId(), subjectId, title, body);
				
				if (LecturesDAO.insert(bean)) {
					if (fileNames.isEmpty()) {
						session.setAttribute("status", "success");
						session.setAttribute("message", "Lecture has been added");
					}
					else {
						bean = LecturesDAO.find(subjectId, title);
						if (FileDAO.insert(bean.getId(), "lectures", fileNames)) {
							session.setAttribute("status", "success");
							session.setAttribute("message", "Lecture has been added");
						}
						else {
							session.setAttribute("status", "danger");
							session.setAttribute("message", "Some troubles were occurred during writing file info to db");
						}
					}
				}
				else {
					session.setAttribute("status", "danger");
					session.setAttribute("message", "Some troubles were occurred during creating a lecture");
				}
			} 
			else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", errorMessage);
			}
			
			response.sendRedirect(request.getContextPath() + "/lectures");
		}
	}

	private boolean lectureBelongSubject(String title, int subjectId, int numExisted) {
		return LecturesDAO.findLecturesCountInSubject(title, subjectId) > numExisted;
	}

	private String lectureValidate(String title, int subjectId, int numExisted) {
		if (lectureBelongSubject(title, subjectId, numExisted)) {
			return "Subject should not contain the same lecture";
		}
		return null;
	}
	
}
