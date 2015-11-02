package beans;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class UserBean {
	private int id;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private byte role;
	private String email;
	private String avatarName = "";
	private int groupId;
	private String groupName;
	private long registered;
	public boolean valid = false;

	public UserBean(String name) {
		this.userName = name;
	}
	
	public UserBean(String name, String password, String email, byte role) {
		this.userName = name;
		this.email = email;
		this.role = role;
		this.setPassword(password);
	}

	public UserBean(String name, String password, String email,
			byte role, String firstName, String lastName, String avatarName, int groupId, long registered) {
		this.userName = name;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatarName = avatarName;
		this.groupId = groupId;
		this.registered = registered;
		this.setPassword(password);
	}
	
	public UserBean(String name, String password, String email,
			byte role, String firstName, String lastName, String avatarName) {
		this.userName = name;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatarName = avatarName;
		this.setPassword(password);
	}
	
	public UserBean(int id, String firstName, String lastName, int groupId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.groupId = groupId;
	}

	public UserBean() { }

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String newLastName) {
		lastName = newLastName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String pass) {
		password = pass;
	}
	
	public boolean isPasswordValid(String plainPass) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		return passwordEncryptor.checkPassword(plainPass, password);
	}

	public String getUsername() {
		return userName;
	}
	public void setUserName(String name) {
		userName = name;
	}

	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean newValid) {
		valid = newValid;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAvatar() {
		return avatarName;
	}
	public void setAvatar(String avatarName) {
		this.avatarName = avatarName;
	}

	public byte getRole() {
		return role;
	}
	public void setRole(byte role) {
		this.role = role;
	}
	
	public int getGroupId() {
	    return groupId;
    }
	public void setGroupId(int groupId) {
	    this.groupId = groupId;
    }
	
	public String getGroupName() {
	    return groupName;
    }
	public void setGroupName(String groupName) {
	    this.groupName = groupName;
    }
	
	public long getRegistered() {
	    return registered;
    }
	public void setRegistered(long registered) {
	    this.registered = registered;
    }
	
	public String toString() {
		return "id: " + id + ", "
				+ "username: " + userName + ", "
				+ "role: " + role + ", "
				+ "email: " + email + ", "
				+ "first name: " + firstName + ", "
				+ "last name: " + lastName + ", "
				+ "Group: " + groupId + ", "
				+ "file name: " + avatarName;
	}

	public String getReadableName() {
		if (firstName != null && lastName != null && !firstName.isEmpty() && !lastName.isEmpty()) {
			return firstName + " " + lastName;
		}
		return userName;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public String getHumanRole() {
		String roleName;
		switch (role) {
			case 0: roleName = "Student";
					break;

			case 1: roleName = "Teacher";
					break;

			case 2: roleName = "Admin";
					break;

			default: roleName = "None";
		}
		return roleName;
	}

	public boolean getAccess(String pageName) {
		if (pageName.matches("register|users")) {
			return this.getRole() == 2;
		}
		else if (pageName.matches("students")) {
			return this.getRole() > 0;
		}
		else if (pageName.matches("practicals")) {
			return this.getRole() > 0;
		}
		else if (pageName.startsWith("/user/")) {
			String[] parts = pageName.split("/");
			if (Integer.valueOf(parts[2]) == this.getId()) {
				return true;
			}
			return this.getRole() == 2;
		}
		return false;
	}
}
