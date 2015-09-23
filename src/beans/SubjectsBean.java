package beans;

public class SubjectsBean {
	private int id;
	private String subjectName;
	private int departmentId;
	
	public SubjectsBean() {}
	
	public SubjectsBean(String subjectName, int departmentId) {
		this.subjectName = subjectName;
		this.departmentId = departmentId;
	}
	
	public SubjectsBean(int id, String subjectName, int departmentId) {
		this.id = id;
		this.subjectName = subjectName;
		this.departmentId = departmentId;
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
	
	public int getDepartmentId() {
	    return departmentId;
    }
	public void setDepartmentId(int departmentId) {
	    this.departmentId = departmentId;
    }
	
	@Override
	public String toString() {
		return "ID: " + id + " Subject: " + subjectName + " Department ID: " + departmentId;
	}
}
