package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import config.ConnectionManager;
import beans.LectureBean;
import beans.UserBean;

public class LectureDAO {
	
	private static Connection con;
	private static ResultSet rs;
	
	
	public static ArrayList<LectureBean> findAll(UserBean user){
		
		ArrayList<LectureBean> lectures = new ArrayList<>();
		LectureBean bean = null;
		
		String query = "SELECT * FROM lectures WHERE id_teacher = ?";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, user.getId());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				bean = new LectureBean();
				bean.setId(rs.getInt("id"));
				bean.setIdTeacher(rs.getInt("idTeacher"));
				bean.setSize(rs.getInt("size"));
				bean.setTitle(rs.getString("title"));
				bean.setFileName(rs.getString("fileName"));
				
				lectures.add(bean);
			}
			
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return lectures;
	}

}
