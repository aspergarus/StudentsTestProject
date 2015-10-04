package beans;

public class AnswerBean {
	
	private int answerId;
	private int questionId;
	private String answerText;
	private boolean isCorrect;
	
	public AnswerBean(int questionId, String answerText, boolean isCorrect) {
		this.setQuestionId(questionId);
		this.setAnswerText(answerText);
		this.setCorrect(isCorrect);
	}
	
	public AnswerBean() { }

	public int getAnswerId() {
	    return answerId;
    }
	public void setAnswerId(int answerId) {
	    this.answerId = answerId;
    }

	public int getQuestionId() {
	    return questionId;
    }
	public void setQuestionId(int questionId) {
	    this.questionId = questionId;
    }

	public String getAnswerText() {
	    return answerText;
    }
	public void setAnswerText(String answerText) {
	    this.answerText = answerText;
    }

	public boolean isCorrect() {
	    return isCorrect;
    }
	public void setCorrect(boolean isCorrect) {
	    this.isCorrect = isCorrect;
    }
}
