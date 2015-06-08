package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import config.ConnectionManager;
import beans.LecturesBean;

public class LecturesDAO {
	
	private static Connection con;
	private static ResultSet rs;
	
	@SuppressWarnings("finally")
	public static Map<String, ArrayList<LecturesBean>> findAll(int teacherId, byte role){
		
		String query;
		
		if (role == 0){
			query = "SELECT * FROM lectures";
		} else {
			query = "SELECT * FROM lectures WHERE teacherId = ? ORDER BY subjectId";
		}
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		
		ArrayList<LecturesBean> lecturesList = new ArrayList<>();
		Map<String, ArrayList<LecturesBean>> lecturesMap = new HashMap<>();
		
		Map<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap();
		String subjectName;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			if (role != 0){
				stmt.setInt(1, teacherId);
			}
			rs = stmt.executeQuery();
			
			int tmpSubjectId = 0, subjectId = 0;
			
			while (rs.next()) {
				subjectId = rs.getInt("subjectId");
				LecturesBean bean = new LecturesBean();
				bean.setId(rs.getInt("id"));
				bean.setTeacherId(teacherId);
				bean.setTitle(rs.getString("title"));
				bean.setSubjectId(subjectId);
				bean.setBody(rs.getString("body"));

				if (tmpSubjectId != subjectId && tmpSubjectId == 0) {
					tmpSubjectId = subjectId;
				}
				if (tmpSubjectId != subjectId) {
					subjectName = subjectsMap.get(tmpSubjectId);
					lecturesMap.put(subjectName, lecturesList);
					
					tmpSubjectId = subjectId;
					lecturesList = new ArrayList<>();
				}
				lecturesList.add(bean);
			}
			if (!lecturesList.isEmpty()) {
				subjectName = subjectsMap.get(subjectId);
				lecturesMap.put(subjectName, lecturesList);
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return lecturesMap;
		}
	}
	
	@SuppressWarnings("finally")
	public static boolean insert(LecturesBean bean) {
		String query = "INSERT INTO lectures "
				+ "(teacherId, subjectId, title, body) "
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
    public static int findLecturesCountInSubject(String title, int subjectId) {
		String query = "SELECT COUNT(*) as lecturesCount FROM practicals "
				+ " WHERE title = ? AND subjectId = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int lecturesCount = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, title);
			stmt.setInt(2, subjectId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				lecturesCount = rs.getInt("lecturesCount");
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return lecturesCount;
		}
	}

	@SuppressWarnings("finally")
	public static LecturesBean find(int id) {
		String query = "SELECT * FROM lectures WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		LecturesBean lBean = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				lBean = new LecturesBean();
				lBean.setId(id);
				lBean.setSubjectId(rs.getInt("subjectId"));
				lBean.setTitle(rs.getString("title"));
				lBean.setBody(rs.getString("body"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return lBean;
		}
	}
	
	@SuppressWarnings("finally")
    public static boolean delete(int id) {
		String query = "DELETE FROM lectures WHERE id = ?";

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
	
	public static boolean update(LecturesBean bean) {
		String query = "UPDATE lectures SET subjectId=?, title=?, body=? WHERE id = ?";

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

	public static LecturesBean find(int subjectId, String title) {
		String query = "SELECT * FROM lectures WHERE subjectId = ? and title = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		LecturesBean lBean = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, subjectId);
			stmt.setString(2, title);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				lBean = new LecturesBean();
				lBean.setId(rs.getInt("id"));
				lBean.setSubjectId(rs.getInt("subjectId"));
				lBean.setTitle(rs.getString("title"));
				lBean.setBody(rs.getString("body"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return lBean;
	}
	
	
}
