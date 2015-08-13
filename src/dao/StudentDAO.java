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
	
	public static ArrayList<String> findAssignedStudents(String studentName, int subjectId) {
		String query = "SELECT u.id, firstName, lastName, groupName FROM users u INNER JOIN stgrelations s ON u.groupId = s.groupId"
				+ " INNER JOIN groups g ON u.groupId = g.id"
				+ " WHERE s.subjectId = ? AND u.role = 0 AND u.id NOT IN (SELECT studentId FROM ready_students)"
				+ " AND (u.firstName LIKE ? OR u.lastName LIKE ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		ArrayList<String> students = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, subjectId);
			stmt.setString(2, "%" + studentName.trim() + "%");
			stmt.setString(3, "%" + studentName.trim() + "%");
			
			rs = stmt.executeQuery();
			
			students = new ArrayList<>();

			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String groupName = rs.getString("groupName");
				students.add("[" + id + "] " + firstName + " " + lastName + " [" + groupName + "]");
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return students;
	}
	
	public static boolean appendToReady(int testId, int studentId) {
		int groupId = UserDAO.getGroupId(studentId);
		String query = "INSERT INTO ready_students (testId, studentId, groupId)"
				+ " VALUES (?, ?, ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			stmt.setInt(2, studentId);
			stmt.setInt(3, groupId);
			
			rowsAffected  = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}
}
