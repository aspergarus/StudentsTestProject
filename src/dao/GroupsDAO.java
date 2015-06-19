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
		String query = "SELECT * FROM groups";
		ArrayList<GroupBean> groups = new ArrayList<>();
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String groupName = rs.getString("groupName");
				GroupBean group = new GroupBean(id, groupName);
				groups.add(group);
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
				String groupName = rs.getString("groupName");
				group = new GroupBean(id, groupName);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
        	return group;
        }
	}
	
	public static String groupValidate (String groupName) {
		String query = "SELECT * FROM groups WHERE groupName = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		String errorMessage = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, groupName);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				errorMessage = "This group is already exist!";
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return errorMessage;
	}
	
	@SuppressWarnings("finally")
    public static boolean insert (String groupName) {
		String query = "INSERT INTO groups " 
					+ "(groupName) "
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
		String query = "UPDATE groups SET groupName = ? WHERE id = ?";
		
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
	        while (rs.next()) {
	        	groupsMap = new HashMap<>();
	        	int groupId = rs.getInt("id");
	        	String groupName = rs.getString("groupName");
	        	groupsMap.put(groupId, groupName);
	        }
		} catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		
		return groupsMap;
	}
	
	@SuppressWarnings("finally")
	public static ArrayList<String> findGroups(String groupName) {
		String query = "SELECT DISTINCT groupName FROM groups WHERE groupName LIKE ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;

		ArrayList<String> groups = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + groupName.trim() + "%");
			rs = stmt.executeQuery();

			groups = new ArrayList<>();

			while (rs.next()) {
				groups.add(rs.getString("groupName"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return groups;
		}
	}
	
	public static boolean shareSubject(int userId, int subjectId, String groups) {
		String[] groupNames = groups.split(",");
		
		ArrayList<Integer> groupsId = findGroupId(groupNames);
		
		ArrayList<Integer> unsharedGroupIds = findUnsharedGroups(userId, subjectId, groupsId);
		if (!unsharedGroupIds.isEmpty()) {
			String insertQuery = "INSERT INTO courses "
					+ "(teacherId, subjectId, groupId) VALUES";
		
			ConnectionManager conM = new ConnectionManager();
			Connection con = conM.getConnection();
			int rowsAffected = 0;
			
			for (int i = 0; i < unsharedGroupIds.size(); i++) {
				insertQuery += " (?,?,?)";
				if (i != unsharedGroupIds.size() - 1) {
					insertQuery += ",";
				}
			}
			
			try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
				int i = 0;
				for (int groupId : unsharedGroupIds) {
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
    public static ArrayList<Integer> findGroupId(String[] groupsName) {
		String query = "SELECT id FROM groups WHERE groupName IN (";

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
	
	public static ArrayList<Integer> findUnsharedGroups(int userId, int subjectId, ArrayList<Integer> groupsId) {
		String selectQuery = "SELECT groupId FROM courses WHERE teacherId=? AND subjectId=? AND groupId IN (";
		
		int size = groupsId.size();
		for (int i = 0; i < size; i++) {
			selectQuery += "?,";
		}
		selectQuery = selectQuery.substring(0, selectQuery.length() - 1) + ")";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		try (PreparedStatement stmt = con.prepareStatement(selectQuery)) {
			stmt.setInt(1, userId);
			stmt.setInt(2, subjectId);
			
			int i = 3;
			for (int id: groupsId) {
				stmt.setInt(i, id);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				groupsId.remove(new Integer(rs.getInt("groupId")));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } 
		
		return groupsId;
	}
}
