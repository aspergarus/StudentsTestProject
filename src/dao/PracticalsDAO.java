package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import beans.PracticalsBean;
import beans.UserBean;
import config.ConnectionManager;

public class PracticalsDAO {
	
	static Connection con = null;
	static ResultSet rs = null;

	@SuppressWarnings("finally")
	public static Map<String, ArrayList<PracticalsBean>> findAll(UserBean user) {
		
		String query;
		
		if (user.getRole() == 0) {
			query = "SELECT * FROM practicals p "
					+ "INNER JOIN stgrelations s ON p.teacher_id = s.teacher_id AND p.subject_id = s.subject_id "
					+ "WHERE s.group_id = ?";
		}
		else if (user.getRole() == 1) {
			query = "SELECT * FROM practicals WHERE teacher_id = ? ORDER BY subject_id";
		}
		else {
			query = "SELECT * FROM practicals ORDER BY subject_id";
		}
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		ArrayList<PracticalsBean> practicalList = new ArrayList<>();
		Map<String, ArrayList<PracticalsBean>> practicalMap = new HashMap<>();
		
		Map<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap();
		String subjectName;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			if (user.getRole() == 0) {
				stmt.setInt(1, user.getGroupId());
			}
			else if (user.getRole() == 1) {
				stmt.setInt(1, user.getId());
			}
			
			rs = stmt.executeQuery();

			int tmpSubjectId = 0, subjectId = 0;
			while (rs.next()) {
				subjectId = rs.getInt("subject_id");
				PracticalsBean bean = new PracticalsBean();
				bean.setId(rs.getInt("id"));
				bean.setTeacherId(user.getId());
				bean.setTitle(rs.getString("title"));
				bean.setSubjectId(subjectId);
				bean.setBody(rs.getString("body"));

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
				+ "(teacher_id, subject_id, title, body) "
				+ "VALUES (?, ?, ?, ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, bean.getTeacherId());
			stmt.setInt(2, bean.getSubjectId());
			stmt.setString(3, bean.getTitle());
			stmt.setString(4, bean.getBody());

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
		String query = "SELECT COUNT(*) as practicals_count FROM practicals "
				+ " WHERE title = ? AND subject_id = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int practicalsCount = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, title);
			stmt.setString(2, subject);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				practicalsCount = rs.getInt("practicals_count");
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
				pBean.setSubjectId(rs.getInt("subject_id"));
				pBean.setTitle(rs.getString("title"));
				pBean.setBody(rs.getString("body"));
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
		String query = "UPDATE practicals SET subject_id=?, title=?, body=? WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		int rowsAffected = 0;
		try (PreparedStatement updateStmt = con.prepareStatement(query)) {
			updateStmt.setInt(1, bean.getSubjectId());
			updateStmt.setString(2, bean.getTitle());
			updateStmt.setString(3, bean.getBody());
			updateStmt.setInt(4, bean.getId());

			rowsAffected = updateStmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}

	public static PracticalsBean find(int subjectId, String title) {
		String query = "SELECT * FROM practicals WHERE subject_id = ? AND title = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		PracticalsBean pBean = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, subjectId);
			stmt.setString(2, title);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				pBean = new PracticalsBean();
				pBean.setId(rs.getInt("id"));
				pBean.setSubjectId(rs.getInt("subject_id"));
				pBean.setTitle(rs.getString("title"));
				pBean.setBody(rs.getString("body"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return pBean;
	}

}
