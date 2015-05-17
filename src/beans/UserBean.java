package beans;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class UserBean {

	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private byte role;
	private String email;
	public boolean valid;
	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

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
		return userName + " " + role + " " + email;
	}

	public String getReadableName() {
		if (!firstName.isEmpty() && !lastName.isEmpty()) {
			return firstName + " " + lastName;
		}
		return userName;
	}

}
