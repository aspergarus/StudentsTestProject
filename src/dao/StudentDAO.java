package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.StudentGroupBean;
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

	public static ArrayList<StudentGroupBean> findAll(UserBean user) {
		String query;
		
		if (user.getRole() == 1) {
			query = "SELECT * FROM users u"
					+ " INNER JOIN stgrelations s ON u.groupId = s.groupId"
					+ " INNER JOIN groups g ON u.groupId = g.id"
					+ " WHERE (u.role = 0 AND s.teacherId = ?)"
					+ " ORDER BY g.groupName";
		}
		else {
			query = "SELECT firstName, lastName, avatarName, groupId, g.groupName FROM users u"
					+ " INNER JOIN groups g ON u.groupId = g.id"
					+ " WHERE u.role = 0"
					+ " ORDER BY g.groupName";
		}
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		ArrayList<StudentGroupBean> groupsList = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			if (user.getRole() == 1) {
				stmt.setInt(1, user.getId());
			}
			ResultSet rs = stmt.executeQuery();
			StudentGroupBean group = new StudentGroupBean();
			String prevGroupName = "";
			
			while (rs.next()) {
				String groupName = rs.getString("groupName");
				
				UserBean student = new UserBean();
				student.setFirstName(rs.getString("firstName"));
				student.setLastName(rs.getString("lastName"));
				student.setGroupName(groupName);
				student.setAvatar(rs.getString("avatarName"));
				
				if (!groupName.equals(prevGroupName)) {
					if (group.size() > 0) {
						groupsList.add(group);
						group = new StudentGroupBean(groupName);
					}
				}
				group.setGroupName(groupName);
				group.add(student);
				prevGroupName = groupName;
			}
			groupsList.add(group);
			
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
			return groupsList;
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
