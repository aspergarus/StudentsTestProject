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

}
