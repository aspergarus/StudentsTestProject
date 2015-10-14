package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import config.ConnectionManager;
import beans.QuestionAnswersBean;
import beans.TestBean;
import beans.UserBean;

public class TestsDAO {
	
	@SuppressWarnings("finally")
    public static HashMap<String, ArrayList<TestBean>> findAll(UserBean user) {
		
		String query = "";
		if (user.getRole() == 2) {
			query = "SELECT * FROM tests";
		} else if (user.getRole() == 1) {
			query = "SELECT * FROM tests WHERE teacher_id = ?";
		} else {
			query = "SELECT * FROM tests t INNER JOIN test_students ts"
					+ " ON t.id = ts.test_id"
					+ " WHERE ts.student_id = ?";
		}
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<TestBean> testsList = new ArrayList<>();
		HashMap<String, ArrayList<TestBean>> testsMap = new HashMap<>();
		HashMap<Integer, String> subjectsMap = SubjectsDAO.getSubjectsMap();
		String subjectName;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
	        if (user.getRole() < 2) {
	        	stmt.setInt(1, user.getId());
	        }
	        rs = stmt.executeQuery();
	        
	        int tmpSubjectId = 0, subjectId = 0;
	        
	        while (rs.next()) {
	        	subjectId = rs.getInt("subject_id");
	        	TestBean test = new TestBean();
	        	test.setId(rs.getInt("id"));
	        	test.setTeacherId(rs.getInt("teacher_id"));
	        	test.setSubjectId(subjectId);
	        	test.setModule(rs.getByte("module"));
	        	test.setNote(rs.getString("note"));
	        	
	        	if (tmpSubjectId != subjectId && tmpSubjectId == 0) {
					tmpSubjectId = subjectId;
				}
				if (tmpSubjectId != subjectId) {
					subjectName = subjectsMap.get(tmpSubjectId);
					testsMap.put(subjectName, testsList);
					
					tmpSubjectId = subjectId;
					testsList = new ArrayList<>();
				}
				testsList.add(test);
			}
			if (!testsList.isEmpty()) {
				subjectName = subjectsMap.get(subjectId);
				testsMap.put(subjectName, testsList);
			} 
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        } finally {
        	return testsMap;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean insert(TestBean newTest) {
		String query = "INSERT INTO tests "
				+ "(teacher_id, subject_id, module, note) "
				+ "VALUES (?, ?, ?, ?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();

		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, newTest.getTeacherId());
			stmt.setInt(2, newTest.getSubjectId());
			stmt.setByte(3, newTest.getModule());
			stmt.setString(4, newTest.getNote());

			rowsAffected = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			return rowsAffected > 0;
		}
	}
	
	
	@SuppressWarnings("finally")
    public static TestBean find(int id) {
		String query = "SELECT * FROM tests WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		TestBean test = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				test = new TestBean();
				test.setId(rs.getInt("id"));
				test.setTeacherId(rs.getInt("teacher_id"));
				test.setSubjectId(rs.getInt("subject_id"));
				test.setModule(rs.getByte("module"));
				test.setNote(rs.getString("note"));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        } finally {
        	return test;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean update(int id, int module) {
		String query = "UPDATE tests SET module = ? WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, module);
			stmt.setInt(2, id);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return rowsAffected > 0;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean update(int id, String note) {
		String query = "UPDATE tests SET note = ? WHERE id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setString(1, note);
			stmt.setInt(2, id);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        } finally {
        	return rowsAffected > 0;
        }
	}
	
	@SuppressWarnings("finally")
    public static boolean delete(int id) {
		String query ="DELETE FROM tests WHERE id = ?";
		
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
	
	public static ArrayList<UserBean> getReadyStudents(int testId) {
		String query = "SELECT u.id, first_name, last_name, u.group_id FROM users u"
				+ " INNER JOIN stgrelations stg ON u.group_id = stg.group_id"
				+ "	INNER JOIN ready_students rs ON rs.student_id = u.id"
				+ " WHERE test_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<UserBean> students = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				int groupId = rs.getInt("group_id");
				UserBean student = new UserBean(id, firstName, lastName, groupId);
				students.add(student);
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return students;
	}
	
	public static boolean removeReadyStudents(int testId) {
		
		String query = "DELETE FROM ready_students"
				+ " WHERE test_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return rowsAffected > 0;
	}
	
	public static boolean removeReadyStudents(int testId, int studentId) {
		
		String query = "DELETE FROM ready_students"
				+ " WHERE test_id = ?"
				+ " AND student_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			stmt.setInt(2, studentId);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return rowsAffected > 0;
	}
	
	public static boolean openTest(int[] studentsId, int testId) {
		
		ArrayList<UserBean> students = getStudents(studentsId, testId);
		
		int listSize = students.size();
		
		if (listSize > 0) {
			String query = "INSERT INTO test_students (test_id, student_id, group_id)"
					+ " VALUES ";
			
			ConnectionManager conM = new ConnectionManager();
			Connection con = conM.getConnection();
			int rowsAffected = 0;
			
			for (int i = 0; i < listSize; i++) {
				query += "(?, ?, ?)";
				if (i != listSize - 1) {
					query += ", ";
				}
			}
					
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				int i = 0;
				for (UserBean student : students) {
					stmt.setInt(i + 1, testId);
					stmt.setInt(i + 2, student.getId());
					stmt.setInt(i + 3, student.getGroupId());
					i +=3;
				}
				rowsAffected = stmt.executeUpdate();
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
	        }
			return rowsAffected > 0;
		} else {
			return false;
		}
	}
	
	public static ArrayList<UserBean> getStudents(int[] studentsId, int testId) {
		int size = studentsId.length;
		String query = "";
		ArrayList<UserBean> students = new ArrayList<>();
		
		if (size > 0) {
			query = "SELECT id, first_name, last_name, group_id FROM users WHERE id IN (";
			for (int i = 0; i < size; i++) {
				query += "?";
				if (i != size - 1) {
					query += ", ";
				}
			}
			query += ")";
				
			ConnectionManager conM = new ConnectionManager();
			Connection con = conM.getConnection();
			ResultSet rs = null;
			
			try (PreparedStatement stmt = con.prepareStatement(query)) {
				for (int i = 0; i < size; i++) {
					stmt.setInt(i + 1, studentsId[i]);
				}
				rs = stmt.executeQuery();
					
				while(rs.next()) {
					int id = rs.getInt("id");
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					int groupId = rs.getInt("group_id");
					
					UserBean student = new UserBean(id, firstName, lastName, groupId);
					students.add(student);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
		    }
		}
		return students;
	}
	
	public static boolean closeTest(int testId, UserBean student) {
		String query = "DELETE FROM test_students"
				+ " WHERE test_id = ?"
				+ " AND student_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			
			stmt.setInt(1, testId);
			stmt.setInt(2, student.getId());
			
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}
	
	public static boolean closeTest(int testId) {
		String query = "DELETE FROM test_students"
				+ " WHERE test_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rowsAffected > 0;
	}
	
	public static boolean canTesting(UserBean user, int testId) {
		if (user.getRole() > 0) {
			return true;
		}
		String query = "SELECT * FROM test_students"
				+ " WHERE student_id = ? AND test_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			
			stmt.setInt(1, user.getId());
			stmt.setInt(2, testId);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        }
		return false;
	}
	
	public static int getResult(List<QuestionAnswersBean> questionAnswers) {
		
		int listSize = questionAnswers.size();
		int [] questionsId = new int[listSize];
		
		for (int i = 0; i < listSize; i++) {
			questionsId[i] = questionAnswers.get(i).getQuestionId();
		}
		HashMap<Integer, int []> questionsMap = QuestionDAO.getQuestionsWithTrueAnswers(questionsId);
		double result = 0;
		
		for (QuestionAnswersBean question : questionAnswers) {
			int questionId = question.getQuestionId();
			int[] userAnswers = question.getAnswers();
			int[] trueAnswers = questionsMap.get(questionId);
			
			// calculate the result
			result += calcMark(userAnswers, trueAnswers);	
		}
		return (int)Math.round(result);
	}
	
	private static double calcMark(int[] userAnswers, int[] trueAnswers) {
		double mark = 0;
		int count = 0;
		int numTrueAnswers = trueAnswers.length;
		int numUserAnswers = userAnswers.length;
		
		if (trueAnswers.length > 1) {
			for (int i = 0; i < numTrueAnswers; i++) {
				for (int j = 0; j < numUserAnswers; j++) {
					if (trueAnswers[i] == userAnswers[j]) {
						count++;
					}
				}
			}
			if (numTrueAnswers >= numUserAnswers) {
				mark = (double)count / numTrueAnswers;
			} else {
				mark = (double) count / numUserAnswers;
			}
			
		} else {
			if (trueAnswers[0] == userAnswers[0]) {
				mark = 1;
			}
		}
		return mark;
	}
	
	public static boolean saveResult(UserBean user, int testId, int result, long completed) {
		String query = "INSERT INTO test_result (student_id, test_id, result, completed)"
				+ " VALUES (?,?,?,?)";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		int rowsAffected = 0;
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, user.getId());
			stmt.setInt(2, testId);
			stmt.setInt(3, result);
			stmt.setLong(4, completed);
			rowsAffected = stmt.executeUpdate();
			
		} catch (SQLException e) {
	        System.out.println(e.getMessage());
        }
		return rowsAffected > 0;
	}
	
	public static ArrayList<Integer> getTestStudents(int testId) {
		String query = "SELECT u.id FROM users u"
				+ "	INNER JOIN test_students ts ON ts.student_id = u.id"
				+ " WHERE ts.test_id = ?";
		
		ConnectionManager conM = new ConnectionManager();
		Connection con = conM.getConnection();
		ResultSet rs = null;
		
		ArrayList<Integer> studentsIdList = new ArrayList<>();
		
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, testId);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int studentId = rs.getInt("id");
				studentsIdList.add(studentId);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
        }
		return studentsIdList;
	}
}
