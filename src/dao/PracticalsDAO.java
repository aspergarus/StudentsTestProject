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
		
		String query = "SELECT * FROM practicals WHERE teacherId = ? ORDER BY subjectId";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		ArrayList<PracticalsBean> practicalList = new ArrayList<>();
		Map<String, ArrayList<PracticalsBean>> practicalMap = new HashMap<>();
		
		Map<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap();
		String subjectName;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, teacherId);
			rs = stmt.executeQuery();

			int tmpSubjectId = 0, subjectId = 0;
			while (rs.next()) {
				subjectId = rs.getInt("subjectId");
				PracticalsBean bean = new PracticalsBean();
				bean.setId(rs.getInt("id"));
				bean.setTeacherId(teacherId);
				bean.setTitle(rs.getString("title"));
				bean.setSubjectId(subjectId);
				bean.setBody(rs.getString("body"));
				bean.setFileName(rs.getString("fileName"));

				if (tmpSubjectId != subjectId && tmpSubjectId == 0) {
					tmpSubjectId = subjectId;
				}
				if (tmpSubjectId != subjectId) {
					subjectName = subjectsMap.get(tmpSubjectId);
					practicalMap.put(subjectName, practicalList);
					
					tmpSubjectId = subjectId;
					practicalList = new ArrayList<>();
				}
				practicalList.add(bean);
			}
			if (!practicalList.isEmpty()) {
				subjectName = subjectsMap.get(subjectId);
				practicalMap.put(subjectName, practicalList);
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
				+ "(teacherId, subjectId, title, body, fileName) "
				+ "VALUES (?, ?, ?, ?, ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, bean.getTeacherId());
			stmt.setInt(2, bean.getSubjectId());
			stmt.setString(3, bean.getTitle());
			stmt.setString(4, bean.getBody());
			stmt.setString(5, bean.getFileName());

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
    public static int findPracticalsCountInSubject(String title, String subject) {
		String query = "SELECT COUNT(*) as practicalsCount FROM practicals "
				+ " WHERE title = ? AND subjectId = ?";

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

	@SuppressWarnings("finally")
    public static PracticalsBean find(int id) {
		String query = "SELECT * FROM practicals WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		PracticalsBean pBean = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				pBean = new PracticalsBean();
				pBean.setId(id);
				pBean.setSubjectId(rs.getInt("subjectId"));
				pBean.setTitle(rs.getString("title"));
				pBean.setBody(rs.getString("body"));
				pBean.setFileName(rs.getString("fileName"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return pBean;
		}
	}

	@SuppressWarnings("finally")
    public static boolean delete(int id) {
		String query = "DELETE FROM practicals WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);

			rowsAffected = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return rowsAffected > 0;
		}
	}

	public static boolean update(PracticalsBean bean) {
		String query = "UPDATE practicals SET subjectId=?, title=?, body=?";
		if (!bean.getFileName().isEmpty()) {
			query += ", fileName=? WHERE id = ?";
		}
		else {
			query += " WHERE id = ?";
		}

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		int rowsAffected = 0;
		try (PreparedStatement updateStmt = con.prepareStatement(query)) {
			updateStmt.setInt(1, bean.getSubjectId());
			updateStmt.setString(2, bean.getTitle());
			updateStmt.setString(3, bean.getBody());
			if (!bean.getFileName().isEmpty()) {
				updateStmt.setString(4, bean.getFileName());
				updateStmt.setInt(5, bean.getId());
			}
			else {
				updateStmt.setInt(4, bean.getId());
			}

			rowsAffected = updateStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}
}
