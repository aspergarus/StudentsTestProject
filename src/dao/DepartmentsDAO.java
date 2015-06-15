package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import beans.DepartmentBean;
import config.ConnectionManager;

public class DepartmentsDAO {
	
	public static ArrayList<DepartmentBean> findAll() {
		String query = "SELECT * FROM departments";
		ArrayList<DepartmentBean> departments = new ArrayList<>();
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String departmentName = rs.getString("departmentName");
				DepartmentBean department = new DepartmentBean(id, departmentName);
				departments.add(department);
			}
        } catch (SQLException e) {
	        System.out.println(e.getMessage());
        }
		return departments;
	}
	
	@SuppressWarnings("finally")
    public static DepartmentBean find(int id) {
		String query = "SELECT * FROM departments WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		DepartmentBean department = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				String departmentName = rs.getString("departmentName");
				department = new DepartmentBean(id, departmentName);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
        	return department;
        }
		
	}
	
	public static String departmentValidate (String departmentName) {
		String query = "SELECT * FROM departments WHERE departmentName = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		String errorMessage = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, departmentName);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				errorMessage = "This department is already exist!";
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return errorMessage;
	}
	
	@SuppressWarnings("finally")
    public static boolean insert (String departmentName) {
		String query = "INSERT INTO departments " 
					+ "(departmentName) "
					+ "VALUES (?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, departmentName);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
			return rowsAffected > 0;
		}
	}
	
	@SuppressWarnings("finally")
    public static boolean delete (int id) {
		String query = "DELETE FROM departments WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
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
	
	@SuppressWarnings("finally")
    public static boolean update (DepartmentBean department) {
		String query = "UPDATE departments SET departmentName = ? WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, department.getDepartmentName());
			stmt.setInt(2, department.getId());
			rowsAffected = stmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
			return rowsAffected > 0;
		}
	}
	
	@SuppressWarnings("finally")
    public static int findDepartmentId(String departmentName) {
		String query = "SELECT id FROM departments WHERE departmentName = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		int id = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, departmentName);
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
	
	public static HashMap<Integer, String> getDepartmentsMap () {
		String query = "SELECT * FROM departments";
		
		HashMap<Integer, String> departmentsMap = null;
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		try (Statement stmnt = con.createStatement()) {
	        rs = stmnt.executeQuery(query);
	        while (rs.next()) {
	        	departmentsMap = new HashMap<>();
	        	int departmentId = rs.getInt("id");
	        	String departmentName = rs.getString("departmentName");
	        	departmentsMap.put(departmentId, departmentName);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		
		return departmentsMap;
	}
}