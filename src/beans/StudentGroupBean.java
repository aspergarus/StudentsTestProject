package beans;

import java.util.ArrayList;

public class StudentGroupBean {

	private String groupName;
	private ArrayList<UserBean> students;
	
	public StudentGroupBean (String groupName) {
		this.setGroupName(groupName);
		this.students = new ArrayList<>();
	}
	
	public StudentGroupBean () {
		this.students = new ArrayList<>();
	}
	
	public String getGroupName() {
	    return groupName;
    }
	public void setGroupName(String groupName) {
	    this.groupName = groupName;
    }

	public ArrayList<UserBean> getStudents() {
	    return students;
    }
	public void setStudents(ArrayList<UserBean> students) {
	    this.students = students;
    }
	public void add (UserBean student) {
		this.students.add(student);
	}
	
	public int size() {
		return this.students.size();
	}
}
