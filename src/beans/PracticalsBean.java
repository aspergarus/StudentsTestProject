package beans;

public class PracticalsBean {
	private int id;
	private int teacherId;
	private int subjectId;
	private String title;
	private String body = "";
	
	public PracticalsBean(int teacher, int subjectId, String title, String body) {
		this.teacherId = teacher;
		this.subjectId = subjectId;
		this.title = title;
		this.body = body;
	}
	
	public PracticalsBean() {}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public String toString() {
		return "Practical. ID: " + id + "."
				+ " Author: " + teacherId + "."
				+ " Title: " + title + "."
				+ " Body: " + body + ".";
	}
}
