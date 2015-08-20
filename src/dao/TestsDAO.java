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
	
	@SuppressWarnings("finally")
    public static boolean update(int id, int module) {
		String query = "UPDATE tests SET module = ? WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, module);
			stmt.setInt(2, id);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return rowsAffected > 0;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean update(int id, String note) {
		String query = "UPDATE tests SET note = ? WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, note);
			stmt.setInt(2, id);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return rowsAffected > 0;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean delete(int id) {
		String query ="DELETE FROM tests WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return rowsAffected > 0;
        }
	}
	
	public static ArrayList<UserBean> getTestStudents(int testId) {
		String query = "SELECT u.id, firstname, lastname, u.groupId FROM users u"
				+ " INNER JOIN stgrelations stg ON u.groupId = stg.groupId"
				+ "	INNER JOIN ready_students ot ON ot.studentId = u.id"
				+ " WHERE testId = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<UserBean> students = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				int groupId = rs.getInt("groupId");
				UserBean student = new UserBean(id, firstName, lastName, groupId);
				students.add(student);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return students;
	}
	
	public static boolean openTest(int[] studentsId, int testId) {
		ArrayList<UserBean> newStudents = getNewStudents(studentsId, testId);
		int listSize = newStudents.size();
		
		if (listSize > 0) {
			String query = "INSERT INTO open_tests (testId, studentId, groupId)"
					+ " VALUES ";
			
			ConnectionManager conM = new ConnectionManager();
			Connection con = conM.getConnection();
			int rowsAffected = 0;
			
			for (int i = 0; i < listSize; i++) {
				query += "(?, ?, ?)";
				if (i != listSize - 1) {
					query += ", ";
				}
			}
					
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				int i = 0;
				for (UserBean student : newStudents) {
					stmt.setInt(i + 1, testId);
					stmt.setInt(i + 2, student.getId());
					stmt.setInt(i + 3, student.getGroupId());
					i +=3;
				}
				rowsAffected = stmt.executeUpdate();
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
	        }
			return rowsAffected > 0;
		} else {
			return false;
		}
	}
	
	public static ArrayList<UserBean> getNewStudents(int[] studentsId, int testId) {
		
		ArrayList<Integer> ids = new ArrayList<>();
		for (int i = 0; i < studentsId.length; i++) {
			ids.add(studentsId[i]);
		}
		ArrayList<UserBean> newStudents = new ArrayList<>();
		
		String query = "SELECT studentId FROM open_tests";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			rs = stmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("studentId");
				ids.remove(new Integer(id));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		
		
		int listSize = ids.size();
		
		if (listSize > 0) {
			query = "SELECT id, firstName, lastName, groupId FROM users WHERE id IN (";
			for (int i = 0; i < listSize; i++) {
				query += "?";
				if (i != listSize - 1) {
					query += ", ";
				}
			}
			query += ")";
			
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				for (int i = 0; i < listSize; i++) {
					stmt.setInt(i + 1, ids.get(i));
				}
				rs = stmt.executeQuery();
				
				while(rs.next()) {
					int id = rs.getInt("id");
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					int groupId = rs.getInt("groupId");
					
					UserBean student = new UserBean(id, firstName, lastName, groupId);
					newStudents.add(student);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
	        }
		}
		
		return newStudents;
	}
	
}
