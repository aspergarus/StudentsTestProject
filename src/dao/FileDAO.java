package dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import beans.FileBean;
import config.ConnectionManager;

public class FileDAO {

	public static boolean insert(int ownerId, String type, ArrayList<String> fileNames) {
		String query = "INSERT INTO files "
				+ "(ownerId, type, name) "
				+ "VALUES (?, ?, ?)";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			for (String fileName : fileNames) {
				stmt.setInt(1, ownerId);
				stmt.setString(2, type);
				stmt.setString(3, fileName);

				rowsAffected += stmt.executeUpdate();
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}

	public static ArrayList<FileBean> findAll(int ownerId, String type) {
		String query = "SELECT * FROM files WHERE ownerId = ? AND type = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ArrayList<FileBean> beans = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, ownerId);
			stmt.setString(2, type);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				FileBean bean = new FileBean();
				bean.setFid(rs.getInt("fid"));
				bean.setOwnerId(rs.getInt("ownerId"));
				bean.setType(rs.getString("type"));
				bean.setName(rs.getString("name"));

				beans.add(bean);
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return beans;
	}

	public static void deleteAll(ArrayList<FileBean> fileBeans) {
		String query = "DELETE FROM files WHERE fid IN (";

		int paramSize = fileBeans.size();
		for (int i = 0; i < paramSize; i++) {
			query += "?,";
		}
		query = query.substring(0, query.length() - 1) + ")";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			for (int i = 0; i < paramSize; i++) {
				stmt.setInt(i + 1, fileBeans.get(i).getFid());
			}
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static FileBean find(int fid) {
		String query = "SELECT * FROM files WHERE fid = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		FileBean bean = null;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, fid);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				bean = new FileBean();
				bean.setFid(rs.getInt("fid"));
				bean.setOwnerId(rs.getInt("ownerId"));
				bean.setType(rs.getString("type"));
				bean.setName(rs.getString("name"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return bean;
	}

	public static void delete(int fid) {
		String query = "DELETE FROM files WHERE fid = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, fid);
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
