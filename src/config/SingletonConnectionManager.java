package config;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SingletonConnectionManager {
	
	private static Connection con;
	private static String url;
	private static String admin;
	private static String password;
	
	private SingletonConnectionManager() {}
	
	private static class SingletonHelper {
		private static final SingletonConnectionManager connectionManager = new SingletonConnectionManager();
	}
	
	public static SingletonConnectionManager getSingletonConnectionManager() {
		return SingletonHelper.connectionManager;
	}

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
