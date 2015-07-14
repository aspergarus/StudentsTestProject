package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import config.ConnectionManager;
import beans.TestBean;
import beans.UserBean;

public class TestsDAO {
	
	@SuppressWarnings("finally")
    public static HashMap<String, ArrayList<TestBean>> findAll(UserBean user) {
		
		String query = "";
		if (user.getRole() == 2) {
			query = "SELECT * FROM tests";
		}
		else if (user.getRole() == 1) {
			query = "SELECT * FROM tests WHERE teacherId = ?";
		}
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<TestBean> testsList = new ArrayList<>();
		HashMap<String, ArrayList<TestBean>> testsMap = new HashMap<>();
		HashMap<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap();
		String subjectName;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
	        if (user.getRole() == 1) {
	        	stmt.setInt(1, user.getId());
	        }
	        rs = stmt.executeQuery();
	        
	        int tmpSubjectId = 0, subjectId = 0;
	        
	        while (rs.next()) {
	        	subjectId = rs.getInt("subjectId");
	        	TestBean test = new TestBean();
	        	test.setId(rs.getInt("id"));
	        	test.setTeacherId(rs.getInt("teacherId"));
	        	test.setSubjectId(subjectId);
	        	test.setModule(rs.getByte("module"));
	        	test.setNote(rs.getString("note"));
	        	
	        	if (tmpSubjectId != subjectId && tmpSubjectId == 0) {
					tmpSubjectId = subjectId;
				}
				if (tmpSubjectId != subjectId) {
					subjectName = subjectsMap.get(tmpSubjectId);
					testsMap.put(subjectName, testsList);
					
					tmpSubjectId = subjectId;
					testsList = new ArrayList<>();
				}
				testsList.add(test);
			}
			if (!testsList.isEmpty()) {
				subjectName = subjectsMap.get(subjectId);
				testsMap.put(subjectName, testsList);
			} 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
        	return testsMap;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean insert(TestBean newTest) {
		String query = "INSERT INTO tests "
				+ "(teacherId, subjectId, module, note) "
				+ "VALUES (?, ?, ?, ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, newTest.getTeacherId());
			stmt.setInt(2, newTest.getSubjectId());
			stmt.setByte(3, newTest.getModule());
			stmt.setString(4, newTest.getNote());

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
    public static TestBean find(int id) {
		String query = "SELECT * FROM tests WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		TestBean test = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				test = new TestBean();
				test.setId(rs.getInt("id"));
				test.setTeacherId(rs.getInt("teacherId"));
				test.setSubjectId(rs.getInt("subjectId"));
				test.setModule(rs.getByte("module"));
				test.setNote(rs.getString("note"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
        	return test;
        }
	}

}
