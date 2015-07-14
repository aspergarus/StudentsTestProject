package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.QuestionBean;
import config.ConnectionManager;

public class QuestionDAO {
	
    public static int add(QuestionBean question) {
		String query = "INSERT INTO questions "
				+ "(testId, questionText) VALUES "
				+ "(?, ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		int result = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			
			stmt.setInt(1, question.getTestId());
			stmt.setString(2, question.getQuestionText());
			
			rowsAffected = stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			
			if (rowsAffected == 0) {
				return rowsAffected;
			}
			
			if (rs.next()){
			    result = rs.getInt(1);
			}
			

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
}
