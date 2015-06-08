package beans;

public class SubjectsBean {
	private int id;
	private String subjectName;
	private String department;
	private int teacherId = 0;
	
	public SubjectsBean() {}
	
	public SubjectsBean(String subjectName, String department) {
		this.subjectName = subjectName;
		this.department = department;
	}
	
	public int getId() {
	    return id;
    }
	public void setId(int id) {
	    this.id = id;
    }
	
	public String getSubjectName() {
	    return subjectName;
    }
	public void setSubjectName(String subjectName) {
	    this.subjectName = subjectName;
    }
	
	public String getDepartment() {
	    return department;
    }
	public void setDepartment(String department) {
	    this.department = department;
    }
	
	public int getTeacherId() {
	    return teacherId;
    }
	public void setTeacherId(int teacherId) {
	    this.teacherId = teacherId;
    }
	
	@Override
	public String toString() {
		return "ID: " + id + " Subject: " + subjectName + " Department: " + department;
	}

	
}
