package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import config.ConnectionManager;
import beans.AnswerBean;

public class AnswersDAO {
	
	@SuppressWarnings("finally")
    public static boolean add(ArrayList<AnswerBean> answers) {
		String query = "INSERT INTO answers "
				+ "(question_id, answer_text, correct) "
				+ "VALUES";
		
		for (int i = 0; i < answers.size(); i++) {
			query += " (?, ?, ?)";
			
			if (i != answers.size() - 1) {
				query += ",";
			}
		}
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			int i = 0;
			for (AnswerBean answer : answers) {
				stmt.setInt(i + 1, answer.getQuestionId());
				stmt.setString(i + 2, answer.getAnswerText());
				stmt.setBoolean(i + 3, answer.isCorrect());
				i += 3;
			}
			rowsAffected = stmt.executeUpdate();
			
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			return rowsAffected > 0;
		}
		
	}

}
