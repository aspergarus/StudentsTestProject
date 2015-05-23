package beans;

public class PracticalsBean {
	private int id;
	private int teacherId;
	private String subject;
	private String title;
	private String body;
	private String filePath;

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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public PracticalsBean(int teacher, String subject, String title, String body, String filePath) {
		this.teacherId = teacher;
		this.subject = subject;
		this.title = title;
		this.body = body;
		this.filePath = filePath;
	}
	public PracticalsBean() {}

	public String toString() {
		return this.title + " " + this.body + " " + this.filePath;
	}
}
