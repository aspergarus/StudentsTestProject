package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import beans.PracticalsBean;
import config.ConnectionManager;

public class PracticalsDAO {
	static Connection con = null;
	static ResultSet rs = null;

	@SuppressWarnings("finally")
	public static Map<String, ArrayList<PracticalsBean>> findAll(int teacherId) {
		String query = "SELECT * FROM practicals WHERE teacherId = ? ORDER BY subject";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		ArrayList<PracticalsBean> practicalList = new ArrayList<>();
		Map<String, ArrayList<PracticalsBean>> practicalMap = new HashMap<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, teacherId);
			rs = stmt.executeQuery();

			String tmpSubject = "", subject = "";
			while (rs.next()) {
				subject = rs.getString("subject");
				PracticalsBean bean = new PracticalsBean();
				bean.setId(rs.getInt("id"));
				bean.setTeacherId(teacherId);
				bean.setTitle(rs.getString("title"));
				bean.setSubject(subject);
				bean.setBody(rs.getString("body"));
				bean.setFilePath(rs.getString("fileName"));

				if (!tmpSubject.equals(subject) && tmpSubject.isEmpty()) {
					tmpSubject = new String(subject);
				}
				if (!tmpSubject.equals(subject)) {
					practicalMap.put(tmpSubject, practicalList);

					tmpSubject = new String(subject);
					practicalList = new ArrayList<>();
				}
				practicalList.add(bean);
			}
			if (!practicalList.isEmpty()) {
				practicalMap.put(subject, practicalList);
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return practicalMap;
		}
	}

	@SuppressWarnings("finally")
	public static boolean insert(PracticalsBean bean) {
		String query = "INSERT INTO practicals "
				+ "(teacherId, subject, title, body, fileName) "
				+ "VALUES (?, ?, ?, ?, ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, bean.getTeacherId());
			stmt.setString(2, bean.getSubject());
			stmt.setString(3, bean.getTitle());
			stmt.setString(4, bean.getBody());
			stmt.setString(5, bean.getFilePath());

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
	public static ArrayList<String> findSubjects(String subjectTitle) {
		String query = "SELECT DISTINCT subject FROM practicals WHERE subject LIKE ?";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		ArrayList<String> subjects = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + subjectTitle.trim() + "%");
			rs = stmt.executeQuery();

			subjects = new ArrayList<>();

			while (rs.next()) {
				subjects.add(rs.getString("subject"));
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
    public static int findPracticalsCountInSubject(String title, String subject) {
		String query = "SELECT COUNT(*) as practicalsCount FROM practicals "
				+ " WHERE title = ? AND subject = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int practicalsCount = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, title);
			stmt.setString(2, subject);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				practicalsCount = rs.getInt("practicalsCount");
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return practicalsCount;
		}
	}

	@SuppressWarnings("finally")
	public static int equivalentFileCount(String fileName) {
		String query = "SELECT COUNT(*) as filesCount FROM practicals "
				+ " WHERE fileName LIKE ? ";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int filesCount = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, fileName + "%");
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				filesCount = rs.getInt("filesCount");
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return filesCount;
		}
	}
}
