package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import config.ConnectionManager;
import beans.SubjectsBean;
import beans.UserBean;

public class SubjectsDAO {
	
	private static Connection con;
	private static ResultSet rs;
	
	public static ArrayList<SubjectsBean> findAll (UserBean user) {
		ArrayList<SubjectsBean> allSubjects = new ArrayList<>();
		String query;
		if (user.getRole() == 2) {
			query = "SELECT * FROM subjects";
		}
		else {
			query = "SELECT * FROM subjects WHERE departmentId = ?";
		}
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
	        
			if (user.getRole() == 1) {
				stmt.setInt(1, user.getGroupId());
			}
			rs = stmt.executeQuery();
	        while (rs.next()) {
	        	SubjectsBean subject = new SubjectsBean();
	        	subject.setId(rs.getInt("id"));
	        	subject.setSubjectName(rs.getString("subjectName"));
	        	subject.setDepartmentId(rs.getInt("departmentId"));
	        	allSubjects.add(subject);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		
		return allSubjects;
	}
	
	@SuppressWarnings("finally")
	public static boolean insert(SubjectsBean bean) {
		String query = "INSERT INTO subjects "
				+ "(subjectName, departmentId) "
				+ "VALUES (?, ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, bean.getSubjectName());
			stmt.setInt(2, bean.getDepartmentId());

			rowsAffected = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return rowsAffected > 0;
		}
	}
	
	@SuppressWarnings("finally")
    public static boolean delete (int id) {
		
		String query = "DELETE FROM subjects WHERE id = ?";
		int rowsAffected = 0;
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			
			rowsAffected = stmt.executeUpdate();
	
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
			return rowsAffected > 0;
		}
	}
	
	@SuppressWarnings("finally")
    public static SubjectsBean find(int id) {
		String query = "SELECT * FROM subjects WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		SubjectsBean sBean = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			rs = stmt.executeQuery();

			if (rs.next()) {
				sBean = new SubjectsBean();
				sBean.setId(id);
				sBean.setSubjectName(rs.getString("subjectName"));
				sBean.setDepartmentId(rs.getInt("departmentId"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return sBean;
		}
	}
	
	public static boolean update(SubjectsBean bean) {
		String query = "UPDATE subjects SET subjectName=?, departmentId=? WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement updateStmt = con.prepareStatement(query)) {
			updateStmt.setString(1, bean.getSubjectName());
			updateStmt.setInt(2, bean.getDepartmentId());
			updateStmt.setInt(3, bean.getId());

			rowsAffected = updateStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}
	
	public static HashMap<Integer, String> getSubjectsMap () {
		String query = "SELECT * FROM subjects";
		
		HashMap<Integer, String> subjectsMap = null;
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		
		try (Statement stmnt = con.createStatement()) {
	        rs = stmnt.executeQuery(query);
	        while (rs.next()) {
	        	subjectsMap = new HashMap<>();
	        	int subjectId = rs.getInt("id");
	        	String subjectName = rs.getString("subjectName");
	        	subjectsMap.put(subjectId, subjectName);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		
		return subjectsMap;
	}
	
	@SuppressWarnings("finally")
	public static ArrayList<String> findSubjects(String subjectTitle) {
		String query = "SELECT DISTINCT subjectName FROM subjects WHERE subjectName LIKE ?";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		ArrayList<String> subjects = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + subjectTitle.trim() + "%");
			rs = stmt.executeQuery();

			subjects = new ArrayList<>();

			while (rs.next()) {
				subjects.add(rs.getString("subjectName"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return subjects;
		}
	}
	
	@SuppressWarnings("finally")
    public static int findSubjectId(String subject) {
		String query = "SELECT id FROM subjects WHERE subjectName = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int id = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, subject);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return id;
        }
	}
	
	public static String subjectValidate(String subjectName, int departmentId) {
		String query = "SELECT * FROM subjects WHERE subjectName = ? AND departmentId = ?";
		String errorMessage = null;
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		try (PreparedStatement updateStmt = con.prepareStatement(query)) {
			updateStmt.setString(1, subjectName);
			updateStmt.setInt(2, departmentId);

			rs = updateStmt.executeQuery();
			if (rs.next()) {
				errorMessage = "This subject is already exist. Change Subject name or Department.";
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return errorMessage;
	}
}