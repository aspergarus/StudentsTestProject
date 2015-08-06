package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.AnswerBean;
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
    
    public static ArrayList<QuestionBean> getQuestions(int testId) {
		String query = "SELECT * FROM questions WHERE testId = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<QuestionBean> questions = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String questionText = rs.getString("questionText");
				
				QuestionBean question = new QuestionBean(id, testId, questionText);
				questions.add(question);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return addAnswersToQuestions(questions);
	}
	
	public static ArrayList<QuestionBean> addAnswersToQuestions(ArrayList<QuestionBean> questions) {
		String query = "SELECT * FROM answers WHERE questionId IN (";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		int listSize = questions.size();
		
		for (int i = 0; i < listSize; i++) {
			query += "?";
			
			if (i != listSize - 1) {
				query += ", ";
			}
		}
		query += ")";
		
		ArrayList<AnswerBean> answers = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			
			for (int i = 0; i < listSize; i++) {
				stmt.setInt(i + 1, questions.get(i).getId());
			}
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int questionId = rs.getInt("questionId");
				String answerText = rs.getString("answerText");
				boolean isCorrect = rs.getBoolean("correct");
				AnswerBean answer = new AnswerBean(questionId, answerText, isCorrect);
				answers.add(answer);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		for (QuestionBean question : questions) {
			for (int i = 0; i < answers.size(); i++) {
				if (answers.get(i).getQuestionId() == question.getId()) {
					question.addAnswer(answers.get(i));
				}
			}
		}
		return questions;
	}
}
