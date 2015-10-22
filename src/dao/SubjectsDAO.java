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
	
	public static ArrayList<SubjectsBean> findAll(UserBean user) {
		String query;
		
		if (user.getRole() == 2) {
			query = "SELECT * FROM subjects";
		} else {
			query = "SELECT * FROM subjects WHERE department_id = ?";
		}
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		ArrayList<SubjectsBean> allSubjects = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
	        
			if (user.getRole() == 1) {
				stmt.setInt(1, user.getGroupId());
			}
			rs = stmt.executeQuery();
	        while (rs.next()) {
	        	SubjectsBean subject = new SubjectsBean();
	        	subject.setId(rs.getInt("id"));
	        	subject.setSubjectName(rs.getString("subject_name"));
	        	subject.setDepartmentId(rs.getInt("department_id"));
	        	allSubjects.add(subject);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		return allSubjects;
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
				sBean.setSubjectName(rs.getString("subject_name"));
				sBean.setDepartmentId(rs.getInt("department_id"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return sBean;
		}
	}
	
	@SuppressWarnings("finally")
    public static int find(String subject) {
		String query = "SELECT id FROM subjects WHERE subject_name = ?";
		
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
	
	@SuppressWarnings("finally")
	public static boolean insert(SubjectsBean bean) {
		String query = "INSERT INTO subjects "
				+ "(subject_name, department_id) "
				+ "VALUES (?, ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, bean.getSubjectName());
			stmt.setInt(2, bean.getDepartmentId());

			rowsAffected = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return rowsAffected > 0;
		}
	}
	
	@SuppressWarnings("finally")
    public static boolean delete(int id) {
		String query = "DELETE FROM subjects WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			rowsAffected = stmt.executeUpdate();
	
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
			return rowsAffected > 0;
		}
	}
	
	public static boolean update(SubjectsBean bean) {
		String query = "UPDATE subjects SET subject_name=?, department_id=? WHERE id = ?";

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
		
		HashMap<Integer, String> subjectsMap = new HashMap<>();
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		
		try (Statement stmnt = con.createStatement()) {
	        rs = stmnt.executeQuery(query);
	        while (rs.next()) {
	        	int subjectId = rs.getInt("id");
	        	String subjectName = rs.getString("subject_name");
	        	subjectsMap.put(subjectId, subjectName);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		return subjectsMap;
	}
	
	@SuppressWarnings("finally")
	public static ArrayList<String> findSubjects(String subjectTitle) {
		String query = "SELECT DISTINCT subject_name FROM subjects WHERE subject_name LIKE ?";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		ArrayList<String> subjects = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + subjectTitle.trim() + "%");
			rs = stmt.executeQuery();
			subjects = new ArrayList<>();

			while (rs.next()) {
				subjects.add(rs.getString("subject_name"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return subjects;
		}
	}
	
	@SuppressWarnings("finally")
    public static int findDepartment(int subjectId) {
		String query = "SELECT department_id FROM subjects WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		int departmentId = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, subjectId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				departmentId = rs.getInt("department_id");
			}
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return departmentId;
        }
	}
	
	public static String subjectValidate(String subjectName, int departmentId) {
		String query = "SELECT * FROM subjects WHERE subject_name = ? AND department_id = ?";
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
