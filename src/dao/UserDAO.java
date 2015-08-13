package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.jasypt.util.password.StrongPasswordEncryptor;

import config.ConnectionManager;
import beans.UserBean;

public class UserDAO {

	static Connection con = null;
	static ResultSet rs = null;

	public static UserBean find(String username) throws SQLException {
		UserBean bean = null;
		
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		String query;
		
		if (username.matches(EMAIL_PATTERN)) {
			query = "SELECT * FROM users WHERE email = ?";
		} else {
			query = "SELECT * FROM users WHERE username = ?";
		}
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			boolean more = rs.next();

			// if user exists set the isValid variable to true
			if (more) {
				bean = new UserBean();
				bean.setId(rs.getInt("id"));
				bean.setUserName(rs.getString("userName"));
				bean.setFirstName(rs.getString("firstName"));
				bean.setLastName(rs.getString("lastName"));
				bean.setGroupId(rs.getInt("groupId"));
				bean.setEmail(rs.getString("email"));
				bean.setRole(rs.getByte("role"));
				bean.setPassword(rs.getString("password"));
				bean.setAvatar(rs.getString("avatarName"));
				bean.setValid(true);
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return bean;
	}

	public static UserBean find(int id) throws SQLException {
		UserBean bean = null;

		String query = "SELECT * FROM users WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			boolean more = rs.next();

			// if user exists set the isValid variable to true
			if (more) {
				bean = new UserBean();
				bean.setId(rs.getInt("id"));
				bean.setUserName(rs.getString("userName"));
				bean.setFirstName(rs.getString("firstName"));
				bean.setLastName(rs.getString("lastName"));
				bean.setGroupId(rs.getInt("groupId"));
				bean.setEmail(rs.getString("email"));
				bean.setRole(rs.getByte("role"));
				bean.setPassword(rs.getString("password"));
				bean.setAvatar(rs.getString("avatarName"));
				bean.setValid(true);
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return bean;
	}

	public static UserBean register(UserBean bean) throws Exception {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

		String username = bean.getUsername();
		String password = passwordEncryptor.encryptPassword(bean.getPassword());
		String email = bean.getEmail();
		byte role = bean.getRole();
		String firstName = bean.getFirstName();
		String lastName = bean.getLastName();
		int groupId = bean.getGroupId();
		
		String query = "INSERT INTO users "
				+ "(username, password, email, role, firstName, lastName, groupId) "
				+ "VALUES (?,?,?,?,?,?,?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
        int rowsAffected = 0;
		try (PreparedStatement insertUser = con.prepareStatement(query)) {
			insertUser.setString(1, username);
	        insertUser.setString(2, password);
	        insertUser.setString(3, email);
	        insertUser.setByte(4, role);
	        insertUser.setString(5, firstName);
	        insertUser.setString(6, lastName);
	        insertUser.setInt(7, groupId);
	        rowsAffected = insertUser.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
        bean.setValid(rowsAffected > 0);
		return bean;
	}

	public static ArrayList<UserBean> findAll() {
		ArrayList<UserBean> users = new ArrayList<UserBean>();;
		UserBean bean = null;

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
		try (Statement stmt = con.createStatement()) {
			String query = "SELECT * FROM users";
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getInt("id"));
				bean.setUserName(rs.getString("userName"));
				bean.setFirstName(rs.getString("firstName"));
				bean.setLastName(rs.getString("lastName"));
				bean.setEmail(rs.getString("email"));
				bean.setRole(rs.getByte("role"));
				bean.setAvatar(rs.getString("avatarName"));
				bean.setGroupId(rs.getInt("groupId"));
				users.add(bean);
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return users.isEmpty() ? null : users;
	}

	public static boolean update(UserBean user, UserBean updatedUser) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String password = passwordEncryptor.encryptPassword(updatedUser.getPassword());
		int id = user.getId();

		boolean setPass = false;

		String query = "UPDATE users SET username=?, email=?, role=?, firstName=?, lastName=?, avatarName=?";
		if (!updatedUser.getPassword().trim().isEmpty()) {
			query += ", password=?";
			setPass = true;
		}
		query += "WHERE id = ?";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();
        int rowsAffected = 0;
		try (PreparedStatement updateUser = con.prepareStatement(query)) {
			updateUser.setString(1, updatedUser.getUsername());
			updateUser.setString(2, updatedUser.getEmail());
			updateUser.setByte(3, updatedUser.getRole());
			updateUser.setString(4, updatedUser.getFirstName());
			updateUser.setString(5, updatedUser.getLastName());
			updateUser.setString(6, updatedUser.getAvatar());
			if (setPass) {
				updateUser.setString(7, password);
				updateUser.setInt(8, id);
			}
			else {
				updateUser.setInt(7, id);
			}

	        rowsAffected = updateUser.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}

	public static ArrayList<String> findStudents(String namePart) {
		String query = "SELECT id, firstName, lastName FROM users "
				+ "WHERE role = 0 AND (firstName LIKE ? OR lastName LIKE ?)";

		ConnectionManager conM = new ConnectionManager();
		con = conM.getConnection();

		ArrayList<String> list = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + namePart.trim() + "%");
			stmt.setString(2, "%" + namePart.trim() + "%");
			rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("firstName") + " " + rs.getString("lastName") + " [" + rs.getInt("id") +"]");
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return list;
	}
	
	public static ArrayList<String> findTeachers(String namePart) {
		String query = "SELECT id, firstName, lastName FROM users "
				+ "WHERE role = 1 AND (firstName LIKE ? OR lastName LIKE ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<String> nameList = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + namePart.trim() + "%");
			stmt.setString(2, "%" + namePart.trim() + "%");
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				nameList.add(rs.getString("firstName") + " " + rs.getString("lastName") + " [" + rs.getInt("id") + "]");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return nameList;
	}
	
	public static HashMap<Integer, String> getTeachersMap() {
		String query = "SELECT id, firstName, lastName FROM users "
				+ "WHERE role = 1";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		HashMap<Integer, String> teacherMap = new HashMap<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int teacherId = rs.getInt("id");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				
				teacherMap.put(teacherId, firstName + " " + lastName);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		
		return teacherMap;
	}
	
	public static int getGroupId(int id) {
		String query = "SELECT groupId FROM users WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		int groupId = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				groupId = rs.getInt("groupId");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return groupId;
	}
}
