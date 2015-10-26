package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import beans.GroupBean;
import config.ConnectionManager;

public class GroupsDAO {
	
	public static ArrayList<GroupBean> findAll() {
		String query = "SELECT g.*, COUNT(u.group_id) as count_students FROM groups g"
				+ " LEFT JOIN users u ON g.id = u.group_id"
				+ " WHERE u.role = 0 OR u.role IS NULL"
				+ " GROUP BY g.group_name";

		ArrayList<GroupBean> groups = new ArrayList<>();
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				int countStudents = rs.getInt("count_students");
				String groupName = rs.getString("group_name");
				GroupBean group = new GroupBean(id, groupName, countStudents);
				groups.add(group);
			}
        } catch (SQLException e) {
	        System.out.println(e.getMessage());
        }
		return groups;
	}
	
	@SuppressWarnings("finally")
	public static ArrayList<String> findGroups(String groupName) {
		String query = "SELECT DISTINCT group_name FROM groups WHERE group_name LIKE ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		ArrayList<String> groups = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + groupName.trim() + "%");
			rs = stmt.executeQuery();

			while (rs.next()) {
				groups.add(rs.getString("group_name"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return groups;
		}
	}
	
	public static ArrayList<String> findAssignedGroups(String groupName, int subjectId) {
		String query = "SELECT DISTINCT group_name FROM groups g INNER JOIN stgrelations s ON g.id = s.group_id"
				+ " WHERE s.subject_id = ? AND g.group_name LIKE ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		ArrayList<String> groups = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, subjectId);
			stmt.setString(2, "%" + groupName.trim() + "%");
			
			rs = stmt.executeQuery();

			while (rs.next()) {
				groups.add(rs.getString("group_name"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return groups;
	}
	
	@SuppressWarnings("finally")
    public static GroupBean find(int id) {
		String query = "SELECT * FROM groups WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		GroupBean group = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				String groupName = rs.getString("group_name");
				group = new GroupBean(id, groupName);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
        	return group;
        }
	}
	
	@SuppressWarnings("finally")
    public static GroupBean find(String groupName) {
		String query = "SELECT * FROM groups WHERE group_name = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		GroupBean group = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, groupName);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				group = new GroupBean(id, groupName);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
        	return group;
        }
	}
	
	public static String groupValidate (String groupName) {
		String query = "SELECT * FROM groups WHERE group_name = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		String errorMessage = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, groupName);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				errorMessage = "Warning! This group is already exist!";
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return errorMessage;
	}
	
	@SuppressWarnings("finally")
    public static boolean insert (String groupName) {
		String query = "INSERT INTO groups " 
					+ "(group_name) "
					+ "VALUES (?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, groupName);
			rowsAffected = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
			return rowsAffected > 0;
		}
	}
	
	@SuppressWarnings("finally")
    public static boolean delete (int id) {
		String query = "DELETE FROM groups WHERE id = ?";
		
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
	
	@SuppressWarnings("finally")
    public static boolean update (GroupBean group) {
		String query = "UPDATE groups SET group_name = ? WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, group.getGroupName());
			stmt.setInt(2, group.getId());
			rowsAffected = stmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
			return rowsAffected > 0;
		}
	}
	
	public static HashMap<Integer, String> getGroupsMap () {
		String query = "SELECT * FROM groups";
		
		HashMap<Integer, String> groupsMap = null;
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		try (Statement stmnt = con.createStatement()) {
	        rs = stmnt.executeQuery(query);
	        groupsMap = new HashMap<>();
	        
	        while (rs.next()) {
	        	int groupId = rs.getInt("id");
	        	String groupName = rs.getString("group_name");
	        	groupsMap.put(groupId, groupName);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		return groupsMap;
	}
	
	public static boolean shareSubject(int userId, int subjectId, String groups) {
		String[] groupNames = groups.split(",");
		
		if (!groupNames[0].equals("")) {
			ArrayList<Integer> groupsId = findGroupsId(groupNames);
			deleteRelations(userId, subjectId);
			String insertQuery = "INSERT INTO stgrelations "
					+ "(teacher_id, subject_id, group_id) VALUES";
			
			ConnectionManager conM = new ConnectionManager();
			Connection con = conM.getConnection();
			int rowsAffected = 0;
				
			for (int i = 0; i < groupsId.size(); i++) {
				insertQuery += " (?,?,?)";
				if (i != groupsId.size() - 1) {
					insertQuery += ",";
				}
			}
				try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
					int i = 0;
					for (int groupId : groupsId) {
						stmt.setInt(i + 1, userId);
						stmt.setInt(i + 2, subjectId);
						stmt.setInt(i + 3, groupId);
						i += 3;
					}
					rowsAffected = stmt.executeUpdate();
				} catch (SQLException e) {
			        System.out.println(e.getMessage());
		        }
				return rowsAffected > 0;
		}
		return false;
	}
	
	@SuppressWarnings("finally")
    public static ArrayList<Integer> findGroupsId(String[] groupsName) {
		String query = "SELECT id FROM groups WHERE group_name IN (";

		ArrayList<Integer> groupIds = new ArrayList<>();
		int length = groupsName.length;
		
		for (int i = 0; i < length; i++) {
			query += "?,";
		}
		query = query.substring(0, query.length() - 1) + ")";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;	
			
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			for (int i = 0; i < length; i++) {
				stmt.setString(i + 1, groupsName[i]);
			}
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				groupIds.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return groupIds;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean deleteRelations(int userId, int subjectId) {
		String selectQuery = "DELETE FROM stgrelations WHERE teacher_id=? AND subject_id=?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(selectQuery)) {
			stmt.setInt(1, userId);
			stmt.setInt(2, subjectId);
			
			rowsAffected = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return rowsAffected > 0;
		}
	}

	/**
	 * Get all groups attached to teacherId and showed relations between groups as String and subjectId as Integer in HashMap.
	 * 
	 * @param teacherId
	 * @return
	 */
	public static HashMap<String, String> getGroupsByTeacher(int teacherId) {
		String selectQuery = "SELECT subject_name, GROUP_CONCAT(group_name) as groups_str "
				+ "FROM stgrelations r "
				+ "INNER JOIN subjects s ON s.id = r.subject_id "
				+ "INNER JOIN groups g ON g.id = r.group_id "
				+ "WHERE teacher_id=? "
				+ "GROUP BY subject_id ";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;

		HashMap<String, String> groups = new HashMap<>();

		try (PreparedStatement stmt = con.prepareStatement(selectQuery)) {
			stmt.setInt(1, teacherId);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String subjectName = rs.getString("subject_name");
				String groupsStr = rs.getString("groups_str");
				groups.put(subjectName, groupsStr);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
		return groups;
	}
	
	@SuppressWarnings("finally")
    public static boolean appendToReady(int testId, int groupId) {
		
		ArrayList<Integer> studentsId = getReadyStudentsId(groupId);
		int listSize = studentsId.size();
		
		if (listSize == 0) {
			return false;
		}
		String insertQuery = "INSERT INTO ready_students (test_id, student_id, group_id) "
				+ "VALUES ";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		for (int i = 0; i < listSize; i++) {
			insertQuery += "(?, ?, ?)";
			if (i != listSize - 1) {
				insertQuery += ", ";
			}
		}
		try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
			int k = 1;
			for (int i = 0; i < listSize; i++) {
				stmt.setInt(k, testId);
				stmt.setInt(k + 1, studentsId.get(i));
				stmt.setInt(k + 2, groupId);
				k += 3;
			}
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return rowsAffected > 0;
		}
	}
	
	public static ArrayList<Integer> getReadyStudentsId (int groupId) {
		String selectQuery = "SELECT id FROM users"
				+ " WHERE role = 0 AND group_id = ?"
				+ " AND id NOT IN (SELECT student_id FROM ready_students)";
		
		ArrayList<Integer> studentsId = new ArrayList<>();
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		try (PreparedStatement stmt = con.prepareStatement(selectQuery)) {
			stmt.setInt(1, groupId);
			rs = stmt.executeQuery();
	
			while (rs.next()) {
				studentsId.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return studentsId;
	}
}
