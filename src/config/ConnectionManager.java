package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnectionManager {

	static Connection con;
	static String url;
	static String admin;
	static String password;

    public Connection getConnection() {
    	
    	try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = classLoader.getResource("resources/config_project.ini").getPath();
			Properties p = new Properties();
			p.load(new FileInputStream(filePath));

			url = p.getProperty("url");
			admin = p.getProperty("admin");
			password = p.getProperty("password");

			Class.forName("com.mysql.jdbc.Driver"); 
			con = DriverManager.getConnection(url, admin, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return con;
		
	}
}

