package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import beans.UserBean;
import config.ConnectionManager;

public class StudentDAO {
	@SuppressWarnings("finally")
	public static boolean insert(String group, int studentId) {
		String query = "INSERT INTO students "
				+ "(groupName, studentId) "
				+ "VALUES (?, ?)";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, group);
			stmt.setInt(2, studentId);

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
	public static Map<String, ArrayList<UserBean>> findAll(UserBean user) {
		String query;
		
		if (user.getRole() == 1) {
			query = "SELECT * FROM users u "
					+ "INNER JOIN stgrelations s ON u.groupId = s.groupId WHERE (u.role = 0 AND s.teacherId = ?)";
		}
		else {
			query = "SELECT * FROM users WHERE role = 0";
		}
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		ArrayList<UserBean> studentList = new ArrayList<>();
		Map<String, ArrayList<UserBean>> studentMap = new HashMap<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			if (user.getRole() == 1) {
				stmt.setInt(1, user.getId());
			}
			ResultSet rs = stmt.executeQuery();

			String tmpGroupName = "", groupName = "";
			int groupId;
			while (rs.next()) {
				groupId = rs.getInt("groupId");
				groupName = GroupsDAO.find(groupId).getGroupName();
				UserBean bean = new UserBean();

				bean.setId(rs.getInt("id"));
				bean.setFirstName(rs.getString("firstName"));
				bean.setLastName(rs.getString("lastName"));

				if (!tmpGroupName.equals(groupName) && tmpGroupName.isEmpty()) {
					tmpGroupName = new String(groupName);
				}
				if (!tmpGroupName.equals(groupName)) {
					studentMap.put(tmpGroupName, studentList);

					tmpGroupName = new String(groupName);
					studentList = new ArrayList<>();
				}
				studentList.add(bean);
			}
			if (!studentList.isEmpty()) {
				studentMap.put(groupName, studentList);
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return studentMap;
		}
	}

	@SuppressWarnings("finally")
	public static boolean delete(int studentId) {
		String query = "DELETE FROM students "
				+ "WHERE studentId = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, studentId);

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
	public static int findStudentCount(int studentId) {
		String query = "SELECT COUNT(*) as countStudents FROM students s "
				+ " WHERE studentId = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int studentsCount = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				studentsCount = rs.getInt("countStudents");
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return studentsCount;
		}
	}
}
