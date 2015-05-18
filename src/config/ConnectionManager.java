package config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

public class ConnectionManager {

	static Connection con;
	static String url;
	static String admin;
	static String password;

	public static Connection getConnection() {
		
		try {
			String filePath = "D:\\Projects\\Eclipse\\StudentsTestProject\\config_project.ini";
			
			Ini ini = new Ini(new FileReader(filePath));
			Ini.Section config = ini.get("database");

	        url = config.get("url");
	        admin = config.get("admin");
	        password = config.get("password");
			
			
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			con = DriverManager.getConnection(url, admin, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return con;
	}
}
