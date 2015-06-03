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

import util.FileUploadManager;
import beans.LecturesBean;
import beans.UserBean;
import dao.LecturesDAO;

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
				Map<String, ArrayList<LecturesBean>> lecturesMap = LecturesDAO.findAll(user.getId(), user.getRole());

				request.setAttribute("lecturesMap", lecturesMap);
				request.setAttribute("currentUser", user);
				request.getRequestDispatcher("lectures.jsp").forward(request, response);
			}
			else {
				// Show practical info, or editing form for practical/.
				String jsp = edit ? "lecture-edit.jsp" : "lecture-view.jsp";

				//Show specific lecture
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

				// Delete file from file system.
				if (!lBean.getFileName().isEmpty()) {
					String savePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
					File file = new File(savePath + File.separator + lBean.getFileName());
						 
					if(file.delete()){
						System.out.println(file.getName() + " is deleted!");
					} else {
						System.out.println("Delete operation is failed.");
					}
				}

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
			
			// Update existed practical
			String updateId = request.getParameter("update-id");
			if (updateId != null) {
				// Get form values.
				String subject = request.getParameter("subject").trim();
				String title = request.getParameter("title").trim();
				String body = request.getParameter("body").trim();

				String errorMessage = lectureValidate(title, subject, 1);

				if (errorMessage == null) {
					LecturesBean lBean = LecturesDAO.find(Integer.valueOf(updateId));

					String filePath = request.getServletContext().getRealPath("") + File.separator + saveDir;
					String uploadedFileName = FileUploadManager.getFileName("upload", request.getParts());
					String fileName = "";
					if (!uploadedFileName.isEmpty()) {
						// Save uploaded file, and retrieve his path.
						fileName = FileUploadManager.uploadFile("upload", filePath, request.getParts());

						// Delete existed file.
						FileUploadManager.delete(filePath + File.separator + lBean.getFileName());
					}

					// Update fields in lecture bean.
					lBean.setTitle(title);
					lBean.setBody(body);
					lBean.setSubject(subject);
					lBean.setFileName(fileName);

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
			
			// Create new practical
			// Save uploaded file, and retrieve his path.
			String fileName = uploadFile("upload", request);
			fileName = fileName == null ? "" : fileName;
			
			// Get form values.
			String subject = request.getParameter("subject").trim();
			String title = request.getParameter("title").trim();
			String body = request.getParameter("body").trim();
			
			String errorMessage = lectureValidate(title, subject, 0);
			
			if (errorMessage == null) {
				// Create new lectures bean.
				LecturesBean bean = new LecturesBean(user.getId(), subject, title, body, fileName);
				
				if (LecturesDAO.insert(bean)) {
					session.setAttribute("status", "success");
					session.setAttribute("message", "Lecture has been added");
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
		int count = LecturesDAO.equivalentFileCount(fileName);
		if (count == 0) {
			return fileFullName;
		}
		return fileName + "_" + (count + 1) + fileExt;
	}
	
	private boolean lectureBelongSubject(String title, String subject, int numExisted) {
		return LecturesDAO.findLecturesCountInSubject(title, subject) > numExisted;
	}

	private String lectureValidate(String title, String subject, int numExisted) {
		if (lectureBelongSubject(title, subject, numExisted)) {
			return "Subject should not contain the same lecture";
		}
		return null;
	}
	
}
