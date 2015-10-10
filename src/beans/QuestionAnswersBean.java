package beans;

public class QuestionAnswersBean {
	
	private int questionId;
	private int [] answers;
	
	public QuestionAnswersBean() { }
	
	public QuestionAnswersBean(int questionId, int... answers) {
		this.setQuestionId(questionId);
		this.setAnswers(answers);
	}

	public int getQuestionId() {
	    return questionId;
	}
	public void setQuestionId(int questionId) {
	    this.questionId = questionId;
    }

	public int [] getAnswers() {
	    return answers;
    }
	public void setAnswers(int [] answers) {
	    this.answers = answers;
    }

}
