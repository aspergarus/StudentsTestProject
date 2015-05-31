package util;

import java.io.File;
import java.util.Collection;

import javax.servlet.http.Part;


public class FileUploadManager {
	public static String createNewFileName(String fileFullName, String dir) {
		String fileExt = null, fileName = null;
		String destination = dir + File.separator + fileFullName;
		File f = new File(destination);

		if (f.exists()) {
			if (fileFullName.indexOf(".") > 0) {
				fileExt = fileFullName.substring(fileFullName.indexOf("."));
				fileName = fileFullName.substring(0, fileFullName.indexOf("."));
			}
			else {
				fileExt = "";
				fileName = fileFullName;
			}

			int counter = 0;
			do {
				destination = dir + File.separator + fileName + '_' + counter++ + fileExt;
				f = new File(destination);
			} while (f.exists());
		}
		return destination;
	}

	public static String uploadFile(String fileFieldName, String savePath, Collection<Part> collection) {
		String fileName = null, filePath = "";

		try {
			for (Part part : collection) {
				String name = part.getName();
				if (name.equals(fileFieldName)) {
					fileName = extractFileName(part);
					if (!fileName.isEmpty()) {
						filePath = createNewFileName(fileName, savePath);
						part.write(filePath);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	public static String extractFileName(Part part) {
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
