package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class InstallConnect {
	
	// Required tables
	private static String[] requiredTables = {"comments", "departments", "files", "groups",
											"lectures", "practicals", "stgrelations", "subjects",
											"users", "tests", "questions", "answers", "open_tests"};
	
	public static int testConnect() {
		
		InstallConnect ConM = new InstallConnect();
		Connection con = ConM.getTestConnection();
		
		if (con == null) {
			return -1;
		}
		else {
			ArrayList<String> existTables = selectExistTables();
			
			return requiredTables.length - existTables.size();
		}
	}
	
	public static boolean insertSettings(String host, String port, String dataBaseName, String admin, String password) {
		
		InstallConnect conM = new InstallConnect();
		
		if (conM.insertDBSettings(host, port, dataBaseName, admin, password)) {
			return true;
		}
		
		return false;
	}
	
	public static ArrayList<String> selectExistTables() {
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs;
		
		DatabaseMetaData databaseMetaData;
		ArrayList<String> existTables = new ArrayList<>();;
		
		try {
            databaseMetaData = con.getMetaData();
            rs = databaseMetaData.getTables(null, null, null, null);
            
            while (rs.next()) {
				existTables.add(rs.getString("TABLE_NAME"));
			}
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return existTables;
	}
	
	@SuppressWarnings("finally")
    public static boolean createTables() {
		
		HashMap<String, String> tablesQuery = new HashMap<>();
		
		// comments
		String commentsQuery = "CREATE TABLE comments "
				+ "(cid int(11) NOT NULL AUTO_INCREMENT, "
				+ "title varchar(45) NOT NULL, "
				+ "body mediumtext NOT NULL, "
				+ "author int(11) NOT NULL, "
				+ "ownerId int(11) NOT NULL, "
				+ "ownerType varchar(45) NOT NULL, "
				+ "date decimal(16,0) NOT NULL, "
				+ "PRIMARY KEY (cid)); ";
		tablesQuery.put("comments", commentsQuery);
		
		// departments
		String departmentsQuery = "CREATE TABLE departments "
						+ "(id int(11) NOT NULL AUTO_INCREMENT,"
						+ "departmentName varchar(128) NOT NULL,"
						+ "PRIMARY KEY (id)); ";
		tablesQuery.put("departments", departmentsQuery);
		
		// files
		String filesQuery = "CREATE TABLE files "
						+ "(fid int(11) NOT NULL AUTO_INCREMENT,"
						+ "type varchar(32) NOT NULL,"
						+ "name varchar(128) NOT NULL,"
						+ "ownerId int(11) unsigned NOT NULL DEFAULT '0',"
						+ "PRIMARY KEY (fid)); ";
		tablesQuery.put("files", filesQuery);
		
		// groups
		String groupsQuery = "CREATE TABLE groups "
						+ "(id int(11) NOT NULL AUTO_INCREMENT, "
						+ "groupName varchar(45) NOT NULL, "
						+ "PRIMARY KEY (id)); ";
		tablesQuery.put("groups", groupsQuery);
		
		// lectures
		String lecturesQuery = "CREATE TABLE lectures "
						+ "(id int(11) NOT NULL AUTO_INCREMENT, "
						+ "teacherId int(11) NOT NULL, "
						+ "title varchar(128) NOT NULL, "
						+ "subjectId int(11) NOT NULL, "
						+ "body mediumtext, "
						+ "PRIMARY KEY (id)); ";
		tablesQuery.put("lectures", lecturesQuery);
		
		// practicals
		String practicalsQuery = "CREATE TABLE practicals "
						+ "(id int(11) NOT NULL AUTO_INCREMENT, "
						+ "teacherId int(11) NOT NULL DEFAULT '0', "
						+ "title varchar(128) NOT NULL DEFAULT '', "
						+ "subjectId int(11) NOT NULL, "
						+ "body mediumtext, "
						+ "PRIMARY KEY (id,teacherId), "
						+ "UNIQUE KEY id_UNIQUE (id)); ";
		tablesQuery.put("practicals", practicalsQuery);
		
		// stgrelations
		String stgrelationsQuery = "CREATE TABLE stgrelations "
				  + "(teacherId int(11) NOT NULL, "
				  + "groupId int(11) NOT NULL, "
				  + "subjectId int(11) NOT NULL, "
				  + "PRIMARY KEY (teacherId,groupId,subjectId)); ";
		tablesQuery.put("stgrelations", stgrelationsQuery);
		
		// subjects
		String subjectsQuery = "CREATE TABLE subjects "
						+ "(id int(11) NOT NULL AUTO_INCREMENT, "
						+ "subjectName varchar(128) NOT NULL, "
						+ "departmentId int(11) NOT NULL, "
						+ "PRIMARY KEY (id)); ";
		tablesQuery.put("subjects", subjectsQuery);
		
		// users
		String usersQuery = "CREATE TABLE users "
						+ "(id int(11) NOT NULL AUTO_INCREMENT, "
						+ "username varchar(128) NOT NULL, "
						+ "password varchar(512) NOT NULL, "
						+ "email varchar(128) NOT NULL, "
						+ "role tinyint(2) DEFAULT '0', "
						+ "firstname varchar(128) DEFAULT NULL, "
						+ "lastname varchar(128) DEFAULT NULL, "
						+ "status tinyint(1) DEFAULT '1', "
						+ "avatarName varchar(128) DEFAULT '', "
						+ "groupId int(11) NOT NULL, "
						+ "PRIMARY KEY (id));";
		tablesQuery.put("users", usersQuery);
		
		//tests
		String testsQuery = "CREATE TABLE tests "
						+ "(id INT(11) NOT NULL AUTO_INCREMENT, "
						+ "teacherId INT(11) NOT NULL, "
						+ "subjectId INT(11) NOT NULL, "
						+ "module TINYINT(4) NOT NULL, "
						+ "note VARCHAR(128) NULL DEFAULT NULL, "
						+ "PRIMARY KEY (id));";
		tablesQuery.put("tests", testsQuery);
		
		//questions
		String questionsQuery = "CREATE TABLE questions "
				+ "(id INT(11) NOT NULL AUTO_INCREMENT, "
				+ "testId INT(11) NOT NULL DEFAULT '0', "
				+ "questionText VARCHAR(128) NOT NULL DEFAULT '0', "
				+ "PRIMARY KEY (questionId));";
		tablesQuery.put("questions", questionsQuery);
		
		//answers
		String answersQuery = "CREATE TABLE answers "
				+ "(id INT(11) NOT NULL AUTO_INCREMENT, "
				+ "questionId INT(11) NOT NULL DEFAULT '0', "
				+ "answerText VARCHAR(64) NOT NULL DEFAULT '0', "
				+ "correct TINYTEXT NOT NULL, "
				+ "PRIMARY KEY (answerId));";
		tablesQuery.put("answers", answersQuery);
		
		//open_tests
		String openTestsQuery = "CREATE TABLE open_tests "
				+ "(testId INT(11) NOT NULL, "
				+ "studentId INT(11) NOT NULL, "
				+ "groupId INT(11) NOT NULL)";
		tablesQuery.put("open_tests", openTestsQuery);
		
		ArrayList<String> existTables = selectExistTables();
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int[] rowsAffected = {};
		
		try {
	        Statement stmt = con.createStatement();

	        for(String table : requiredTables) {
	        	if (!existTables.contains(table)) {
	        		stmt.addBatch(tablesQuery.get(table));
	        	}
	        }
	        rowsAffected = stmt.executeBatch();
		
        } catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return rowsAffected.length > 0;
        }
	}
	
	public Connection getTestConnection() {
	
		ClassLoader classLoader = getClass().getClassLoader();
	
		String directoryPath = classLoader.getResource("resources").getPath();
	
		File file = new File(directoryPath + "/config_project.ini");
	
		if (!file.exists()) {
			try {
				System.out.println("Creating New File.");
				file.createNewFile();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
	
		Connection con = null;
		
		String filePath = classLoader.getResource("resources/config_project.ini").getPath();
		Properties p = new Properties();
	
		try {
			p.load(new FileInputStream(filePath));
		
			String url = p.getProperty("url");
			String admin = p.getProperty("admin");
			String password = p.getProperty("password");
		
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, admin, password);
		
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
				
		return con;
	}
	
	public boolean insertDBSettings(String host, String port, String dataBaseName, String admin, String password) {
		
		ClassLoader classLoader = getClass().getClassLoader();
		String filePath = classLoader.getResource("resources/config_project.ini").getPath();
				
		Properties p = new Properties();
		String url = "jdbc:mysql://" + host + ":" + port + "/" + dataBaseName;
		
		try {
			p.setProperty("url", url);
			p.setProperty("admin", admin);
			p.setProperty("password", password);
			p.store(new FileOutputStream(filePath), null);
		
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		return false;
		}	
		
		return true;
	}
}
