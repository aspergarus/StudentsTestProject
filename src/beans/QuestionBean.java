package beans;

import java.util.ArrayList;
import java.util.List;

public class QuestionBean {
	
	private int id;
	private int testId;
	private String questionText;
	private ArrayList<AnswerBean> answers;
	private List<String> trueAnswers;
	
	public QuestionBean(int id, int testId, String questionText) {
		this.setId(id);
		this.setTestId(testId);
		this.setQuestionText(questionText);
		answers = new ArrayList<>();
	}
	
	public QuestionBean(int id, int testId, List<String> trueAnswers) {
		this.setId(id);
		this.setTestId(testId);
		this.setTrueAnswers(trueAnswers);
	}
	
	public QuestionBean(int testId, String questionText) {
		this.setTestId(testId);
		this.setQuestionText(questionText);
	}
	
	public QuestionBean () { }

	public int getId() {
	    return id;
    }
	public void setId(int questionId) {
	    this.id = questionId;
    }

	public int getTestId() {
	    return testId;
    }
	public void setTestId(int testId) {
	    this.testId = testId;
    }

	public String getQuestionText() {
	    return questionText;
    }
	public void setQuestionText(String questionText) {
	    this.questionText = questionText;
    }

	public ArrayList<AnswerBean> getAnswers() {
	    return answers;
    }

	public void setAnswers(ArrayList<AnswerBean> answers) {
	    this.answers = answers;
    }
	
	@Override
	public String toString() {
		return "Test ID: " + testId + ". Question ID: " + id + ". " + questionText
				+ "\n" + getAnswersText();
	}
	
	public String getAnswersText() {
		String answers = "";
		for (AnswerBean answer : this.answers) {
			answers += answer.getAnswerText();
			answers += "\n";
		}
		return answers;
	}
	
	public void addAnswer(AnswerBean answer) {
		answers.add(answer);
	}

	public List<String> getTrueAnswers() {
	    return trueAnswers;
    }

	public void setTrueAnswers(List<String> trueAnswers) {
	    this.trueAnswers = trueAnswers;
    }
}
