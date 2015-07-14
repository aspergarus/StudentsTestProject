package beans;

public class QuestionBean {
	
	private int questionId;
	private int testId;
	private String questionText;
	
	public QuestionBean(int testId, String questionText) {
		this.setTestId(testId);
		this.setQuestionText(questionText);
	}
	
	public QuestionBean () { }

	public int getQuestionId() {
	    return questionId;
    }
	public void setQuestionId(int questionId) {
	    this.questionId = questionId;
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
	
}
