package beans;

public class TestResultBean {
	
	private int groupId;
	private String firstName;
	private String lastName;
	private long completedTest;
	private int result;
	
	public TestResultBean(int groupId, String firstName, String lastName, long completedTest, int result) {
		this.groupId = groupId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.completedTest = completedTest;
		this.result = result;
	}
	
	public TestResultBean() { }
	
	public String getFirstName() {
	    return firstName;
    }
	public void setFirstName(String firstName) {
	    this.firstName = firstName;
    }
	public int getGroupId() {
	    return groupId;
    }
	public void setGroupId(int groupId) {
	    this.groupId = groupId;
    }
	public long getCompletedTest() {
	    return completedTest;
    }
	public void setCompletedTest(long completedTest) {
	    this.completedTest = completedTest;
    }
	public String getLastName() {
	    return lastName;
    }
	public void setLastName(String lastName) {
	    this.lastName = lastName;
    }
	public int getResult() {
	    return result;
    }
	public void setResult(int result) {
	    this.result = result;
    }
}
