package beans;

public class QuestionAnswersBean {
	
	private int questionId;
	private int [] answers;
	
	public QuestionAnswersBean(int questionId, int... answers) {
		this.setQuestionId(questionId);
		this.setAnswers(answers);
	}
	
	public QuestionAnswersBean() { }

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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < answers.length; i++) {
			sb.append(answers[i]);
			if (i < answers.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return "Question ID: " + questionId + "." + " Answers ID: " + sb.toString();
	}
}
