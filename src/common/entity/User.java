package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;

public abstract class User implements Serializable {


	private String usrName;
	private String password;
	private String firstName;
	private String lastName;
	private Integer id;
	private String email;
	private UserType userType;
	
	public User(String usrName, String password, String firstName, String lastName, Integer id, String email, UserType userType) {
		this.usrName = usrName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.email = email;
		this.userType = userType;
	}
	
	public String toString(){
		return usrName + password + firstName + lastName + id + email + userType;
	}
	
	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}
