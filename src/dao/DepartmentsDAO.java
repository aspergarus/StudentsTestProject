package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	

}
