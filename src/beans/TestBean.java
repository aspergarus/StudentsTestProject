package beans;

public class TestBean {
	
	private int id;
	private int teacherId;
	private int subjectId;
	private byte module;
	private String note;
	private int time;
	private int testQuestions;
	
	public TestBean (int teacherId, int subjectId, byte module, String note, int time, int testQuestions) {
		this.teacherId = teacherId;
		this.subjectId = subjectId;
		this.module = module;
		this.note = note;
		this.time = time;
		this.setTestQuestions(testQuestions);
	}
	
	public TestBean() { }

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

	public int getSubjectId() {
	    return subjectId;
    }
	public void setSubjectId(int subjectId) {
	    this.subjectId = subjectId;
    }

	public byte getModule() {
	    return module;
    }
	public void setModule(byte module) {
	    this.module = module;
    }

	public String getNote() {
	    return note;
    }
	public void setNote(String note) {
	    this.note = note;
    }

	public int getTime() {
	    return time;
    }
	public void setTime(int time) {
	    this.time = time;
    }

	public int getTestQuestions() {
	    return testQuestions;
    }

	public void setTestQuestions(int testQuestions) {
	    this.testQuestions = testQuestions;
    }
}
