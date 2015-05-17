package config;

import java.sql.*;

public class ConnectionManager {

	static Connection con;
	static String url;

	public static Connection getConnection() {

		String url = "jdbc:mysql://localhost:3306/ourproject";

		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			con = DriverManager.getConnection(url, "admin", "admin");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return con;
	}
}
