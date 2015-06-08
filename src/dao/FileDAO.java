package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import beans.FileBean;
import config.ConnectionManager;

public class FileDAO {

	public static boolean insert(int ownerId, String path, ArrayList<String> fileNames) {
		String query = "INSERT INTO files "
				+ "(ownerId, path, name) "
				+ "VALUES (?, ?, ?)";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			for (String fileName : fileNames) {
				stmt.setInt(1, ownerId);
				stmt.setString(2, path);
				stmt.setString(3, fileName);

				rowsAffected += stmt.executeUpdate();
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}

	public static ArrayList<FileBean> findAll(int ownerId) {
		String query = "SELECT * FROM files WHERE ownerId = ?";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ArrayList<FileBean> beans = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, ownerId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				FileBean bean = new FileBean();
				bean.setFid(rs.getInt("fid"));
				bean.setOwnerId(rs.getInt("ownerId"));
				bean.setPath(rs.getString("path"));
				bean.setName(rs.getString("name"));
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return beans;
	}

	public static void deleteAll(ArrayList<FileBean> fileBeans) {
		String query = "DELETE FROM files WHERE fid IN (?)";

		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		String fids = fileBeans.stream()
			.map(i -> String.valueOf(i.getFid()))
			.collect(Collectors.joining(", "));

		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, fids);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
