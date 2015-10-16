package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import beans.AnswerBean;
import beans.FileBean;
import beans.QuestionBean;
import config.ConnectionManager;

public class QuestionDAO {
	
    public static int add(QuestionBean question) {
		String query = "INSERT INTO questions "
				+ "(test_id, question_text) VALUES "
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
		String query = "SELECT * FROM questions q"
				+ " LEFT JOIN files f ON q.id = f.owner_id"
				+ " WHERE test_id = ?"
				+ " ORDER BY q.id";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<QuestionBean> questions = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String questionText = rs.getString("question_text");
				QuestionBean question = new QuestionBean(id, testId, questionText);
				
				int fileId = rs.getInt("fid");
				if (fileId != 0) {
					int questionId = rs.getInt("owner_id");
					String type = rs.getString("type");
					String name = rs.getString("name");
					FileBean bean = new FileBean(fileId, type, name, questionId);
					question.setImage(bean);
				}
				questions.add(question);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		if (questions.size() > 0) {
			return addAnswersToQuestions(questions);
		} else {
			return questions;
		}
	}
	
	public static ArrayList<QuestionBean> addAnswersToQuestions(ArrayList<QuestionBean> questions) {
		String query = "SELECT * FROM answers WHERE question_id IN (";
		
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
				int answerId = rs.getInt("id");
				int questionId = rs.getInt("question_id");
				String answerText = rs.getString("answer_text");
				boolean isCorrect = rs.getBoolean("correct");
				AnswerBean answer = new AnswerBean(answerId, questionId, answerText, isCorrect);
				answers.add(answer);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		for (QuestionBean question : questions) {
			for (int i = 0; i < answers.size(); i++) {
				if (answers.get(i).getQuestionId() == question.getId()) {
					question.addAnswer(answers.get(i));
					if (answers.get(i).isCorrect()) {
						question.addTrueAnswers();
					}
				}
			}
		}
		return questions;
	}
	
	public static boolean delete(int questionId) {
		String query = "DELETE FROM questions WHERE id = ?";
		
		if (deleteAnswers(questionId)) {
			
			ConnectionManager conM = new ConnectionManager();
			Connection con = conM.getConnection();
			int rowsAffected = 0;
			
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				stmt.setInt(1, questionId);
				
				rowsAffected = stmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			return rowsAffected > 0;
		} else {
			return false;
		}
	}
	
	public static boolean deleteAnswers(int questionId) {
		String query = "DELETE FROM answers WHERE question_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, questionId);
			
			rowsAffected = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}
	
	public static HashMap<Integer, int[]> getQuestionsWithTrueAnswers(int[] questionsId) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < questionsId.length; i++) {
			sb.append("?");
			if (i < questionsId.length - 1) {
				sb.append(",");
			}
		}
		String symbols = sb.toString();
		
		String query = "SELECT q.id, GROUP_CONCAT(a.id) as answer_id FROM questions q"
				+ " INNER JOIN answers a"
				+ " ON q.id = a.question_id"
				+ " WHERE q.id IN ("
				+ symbols
				+ ")"
				+ " AND a.correct = 1"
				+ " GROUP BY q.id";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		HashMap<Integer, int[]> questions = new HashMap<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			for (int i = 0; i < questionsId.length; i++) {
				stmt.setInt(i + 1, questionsId[i]);
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				int questionId = rs.getInt("id");
				String answerIds = rs.getString("answer_id");
				// Convert String Array to int Array
				int[] answers = Arrays.asList(answerIds.split(",")).stream().mapToInt(Integer::parseInt).toArray();
				
				questions.put(questionId, answers);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return questions;
	}
}
