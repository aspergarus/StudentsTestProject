package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.PracticalsBean;
import beans.UserBean;
import config.ConnectionManager;

public class StudentDAO {
	@SuppressWarnings("finally")
	public static boolean insert(int teacherId, int studentId) {
		String query = "INSERT INTO students "
				+ "(teacherId, studentId) "
				+ "VALUES (?, ?)";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, teacherId);
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
	public static ArrayList<UserBean> findAll(int teacherId) {
		String query = "SELECT * FROM students s "
				+ "INNER JOIN users u ON s.studentId = u.id "
				+ "WHERE s.teacherId = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		ArrayList<UserBean> studentList = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, teacherId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserBean bean = new UserBean();

				bean.setId(rs.getInt("studentId"));
				bean.setFirstName(rs.getString("firstName"));
				bean.setLastName(rs.getString("lastName"));

				studentList.add(bean);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally {
			return studentList;
		}
	}

	@SuppressWarnings("finally")
	public static boolean delete(int teacherId, int studentId) {
		String query = "DELETE FROM students "
				+ "WHERE teacherId = ? AND studentId = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, teacherId);
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
}
