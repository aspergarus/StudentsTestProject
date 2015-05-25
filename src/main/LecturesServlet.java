package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

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
	private static final String SAVE_DIR = "uploadLecturesFiles";
       
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
			
			// TODO: LecturesMap for students!
			Map<String, ArrayList<LecturesBean>> lecturesMap = LecturesDAO.findAll(user.getId(), user.getRole());
	
			Set<String> keys = lecturesMap.keySet();
			for (String key : keys) {
				ArrayList<LecturesBean> list = lecturesMap.get(key);
			}
				request.setAttribute("lecturesMap", lecturesMap);
				request.setAttribute("userRole", user.getRole());
				request.getRequestDispatcher("lectures.jsp").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(false);
		UserBean user = (session != null) ? (UserBean) session.getAttribute("user") : null;
		if (user == null) {
			response.sendError(403);
		}
		else {
			String appPath = request.getServletContext().getRealPath("");
			String savePath = appPath + File.separator + SAVE_DIR;
			request.setAttribute("path", savePath);
			
			// creates the save directory if it does not exists
			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
			}
			
			String fileName = null, filePath = "";
	       
			// Save uploaded file, and retrieve his path.
	        for (Part part : request.getParts()) {
	        	String name = part.getName();
	        	if (name.equals("upload")) {
	        		fileName = extractFileName(part);
	        		if (!fileName.isEmpty()) {
	        			filePath = SAVE_DIR + File.separator + fileName;
		                part.write(savePath + File.separator + fileName);
	        		}
	        	}
	        }
	        
	        // Get form values.
	        String subject = request.getParameter("subject").trim();
	        String title = request.getParameter("title").trim();
			String body = request.getParameter("body").trim();

			// Create new lectures bean.
			LecturesBean bean = new LecturesBean(user.getId(), subject, title, body, filePath);

			if (LecturesDAO.insert(bean)) {
				session.setAttribute("status", "success");
				session.setAttribute("message", "Lecture has been added");
			}
			else {
				session.setAttribute("status", "danger");
				session.setAttribute("message", "Some troubles were occurred during creating a lecture");
			}
			response.sendRedirect(request.getContextPath() + "/lectures");
		
		}
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
		
	
}
