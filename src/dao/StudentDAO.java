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
				+ "(group_name, student_id) "
				+ "VALUES (?, ?)";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, group);
			stmt.setInt(2, studentId);

			rowsAffected = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return rowsAffected > 0;
		}
	}

	public static ArrayList<StudentGroupBean> findAll(UserBean user) {
		String query;
		
		if (user.getRole() == 1) {
			query = "SELECT * FROM users u"
					+ " INNER JOIN stgrelations s ON u.group_id = s.group_id"
					+ " INNER JOIN groups g ON u.group_id = g.id"
					+ " WHERE (u.role = 0 AND s.teacher_id = ?)"
					+ " ORDER BY g.group_name";
		} else {
			query = "SELECT first_name, last_name, avatar_name, group_id, g.group_name FROM users u"
					+ " INNER JOIN groups g ON u.group_id = g.id"
					+ " WHERE u.role = 0"
					+ " ORDER BY g.group_name";
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
				String groupName = rs.getString("group_name");
				
				UserBean student = new UserBean();
				student.setFirstName(rs.getString("first_name"));
				student.setLastName(rs.getString("last_name"));
				student.setGroupName(groupName);
				student.setAvatar(rs.getString("avatar_name"));
				
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
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return groupsList;
	}

	@SuppressWarnings("finally")
	public static int findStudentCount(int studentId) {
		String query = "SELECT COUNT(*) as count_students FROM students s "
				+ " WHERE student_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int studentsCount = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				studentsCount = rs.getInt("count_students");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return studentsCount;
		}
	}
	
	public static ArrayList<String> findAssignedStudents(String studentName, int subjectId) {
		String query = "SELECT u.id, first_name, last_name, group_name FROM users u"
				+ "	INNER JOIN stgrelations s ON u.group_id = s.group_id"
				+ " INNER JOIN groups g ON u.group_id = g.id"
				+ " WHERE s.subject_id = ? AND u.role = 0 AND u.id NOT IN (SELECT student_id FROM ready_students)"
				+ " AND (u.first_name LIKE ? OR u.last_name LIKE ?)";
		
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
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String groupName = rs.getString("group_name");
				students.add("[" + id + "] " + firstName + " " + lastName + " [" + groupName + "]");
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return students;
	}
	
	public static boolean appendToReady(int testId, int studentId) {
		String query = "INSERT INTO ready_students (test_id, student_id, group_id)"
				+ " VALUES (?, ?, ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		int groupId = UserDAO.getGroupId(studentId);
		
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
