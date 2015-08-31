package beans;

public class GroupBean {
	
	private int id;
	private String groupName;
	private int countStudents;
	
	public GroupBean(int id, String groupName, int countStudents) {
		this.id = id;
		this.groupName = groupName;
		this.countStudents = countStudents;
	}
	
	public GroupBean(int id, String groupName) {
		this.id = id;
		this.groupName = groupName;
	}
	
	public int getId() {
	    return id;
    }
	public void setId(int id) {
	    this.id = id;
    }
	
	public String getGroupName() {
	    return groupName;
    }
	public void setGroupName(String groupName) {
	    this.groupName = groupName;
    }
	
	public int getCountStudents() {
	    return countStudents;
    }
	public void setCountStudents(int countStudents) {
	    this.countStudents = countStudents;
    }
	
	@Override
	public String toString() {
		return "Group: " + groupName + ". ID: " + id + ". Students: " + countStudents;
	}

}
