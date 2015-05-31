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
	public boolean valid = false;

	public UserBean(String name) {
		this.userName = name;
	}

	public UserBean(String name, String password, String email,
			byte role, String firstName, String lastName) {
		this.userName = name;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setPassword(password);
	}

	public UserBean() {}

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

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}
	
	public String toString() {
		return "id: " + id + ", "
				+ "username: " + userName + ", "
				+ "role: " + role + ", "
				+ "email: " + email + ", "
				+ "first name: " + firstName + ", "
				+ "last name: " + lastName;
	}

	public String getReadableName() {
		if (!firstName.isEmpty() && !lastName.isEmpty()) {
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
