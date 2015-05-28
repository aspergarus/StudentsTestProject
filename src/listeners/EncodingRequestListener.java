package listeners;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class Test
 *
 */
@WebListener
public class EncodingRequestListener implements ServletRequestListener {

    /**
     * Default constructor. 
     */
    public EncodingRequestListener() {}

	/**
     * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
     */
    public void requestDestroyed(ServletRequestEvent arg0)  {}

	/**
     * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
     */
    public void requestInitialized(ServletRequestEvent arg0)  { 
        ServletRequest request = arg0.getServletRequest();
        try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
        // Creating directories for upload files.
        String[] directories = {"files", "uploadLecturesFiles", "uploadPracticalFiles", "UploadAvatars"};
        String appPath = request.getServletContext().getRealPath("");
        
        for(String directory : directories) {
        	String savePath = appPath + File.separator + directory;
        	File fileSaveDir = new File(savePath);
        	if (!fileSaveDir.exists()) {
    	    	fileSaveDir.mkdir();
    	    	System.out.println("Directory created: " + savePath);
        	}
        	if (directory.equals("files")) {
	    		appPath = savePath;
	    	}
        }
    }
}
