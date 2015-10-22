package beans;

public class DepartmentBean {
	
	private int id;
	private String departmentName;
	
	public DepartmentBean(int id, String departmentName) {
		this.id = id;
		this.departmentName = departmentName;
	}
	
	public DepartmentBean() { }
	
	public int getId() {
	    return id;
    }
	public void setId(int id) {
	    this.id = id;
    }
	
	public String getDepartmentName() {
	    return departmentName;
    }
	public void setDepartmentName(String departmentName) {
	    this.departmentName = departmentName;
    }
	
	@Override
	public String toString() {
		return "Department: " + departmentName + ". ID: " + id;
	}
}
