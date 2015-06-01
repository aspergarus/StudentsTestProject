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
			query = "SELECT * FROM lectures WHERE teacherId = ? ORDER BY subject";
		}
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		
		ArrayList<LecturesBean> lecturesList = new ArrayList<>();
		Map<String, ArrayList<LecturesBean>> lecturesMap = new HashMap<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			if (role != 0){
				stmt.setInt(1, teacherId);
			}
			rs = stmt.executeQuery();
			
			String tmpSubject = "", subject = "";
			while (rs.next()) {
				subject = rs.getString("subject");
				LecturesBean bean = new LecturesBean();
				bean.setId(rs.getInt("id"));
				bean.setTeacherId(teacherId);
				bean.setTitle(rs.getString("title"));
				bean.setSubject(subject);
				bean.setBody(rs.getString("body"));
				bean.setFileName(rs.getString("filename"));

				if (!tmpSubject.equals(subject) && tmpSubject.isEmpty()) {
					tmpSubject = new String(subject);
				}
				if (!tmpSubject.equals(subject)) {
					lecturesMap.put(tmpSubject, lecturesList);

					tmpSubject = new String(subject);
					lecturesList = new ArrayList<>();
				}
				lecturesList.add(bean);
			}
			if (!lecturesList.isEmpty()) {
				lecturesMap.put(subject, lecturesList);
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
				+ "(teacherId, subject, title, body, filename) "
				+ "VALUES (?, ?, ?, ?, ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, bean.getTeacherId());
			stmt.setString(2, bean.getSubject());
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
	public static ArrayList<String> findSubjects(String subjectTitle) {
		String query = "SELECT DISTINCT subject FROM lectures WHERE subject LIKE ?";
		
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
	public static int equivalentFileCount(String fileName) {
		String query = "SELECT COUNT(*) as filesCount FROM lectures "
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
				lBean.setSubject(rs.getString("subject"));
				lBean.setTitle(rs.getString("title"));
				lBean.setBody(rs.getString("body"));
				lBean.setFileName(rs.getString("fileName"));
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
}
