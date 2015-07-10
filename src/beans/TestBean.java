package beans;

public class TestBean {
	
	private int id;
	private int teacherId;
	private int subjectId;
	private byte module;
	private String note;
	
	public TestBean (int teacherId, int subjectId, byte module, String note) {
		this.teacherId = teacherId;
		this.subjectId = subjectId;
		this.module = module;
		this.note = note;
	}
	
	public TestBean() {}

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
	
	
}
