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

	public Connection getConnection() {
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = classLoader.getResource("resources/config_project.ini").getPath();
			
			Ini ini = new Ini(new FileReader(filePath));
			Ini.Section config = ini.get("database");

	        url = config.get("url");
	        admin = config.get("admin");
	        password = config.get("password");
			
			
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
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
