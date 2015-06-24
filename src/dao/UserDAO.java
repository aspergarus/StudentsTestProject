package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jasypt.util.password.StrongPasswordEncryptor;

import config.SingletonConnectionManager;
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
		
		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
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
		
		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
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

		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
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

		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
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

		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
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

		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
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

	public static ArrayList<String> findStudentGroups(String namePart) {
		String query = "SELECT distinct group FROM users WHERE role = 0 AND groupName LIKE ?";

		SingletonConnectionManager conM = SingletonConnectionManager.getSingletonConnectionManager();
		con = conM.getConnection();

		ArrayList<String> list = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, "%" + namePart.trim() + "%");
			rs = stmt.executeQuery();

			while (rs.next()) {
				list.add(rs.getString("group"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return list;
    }
}
