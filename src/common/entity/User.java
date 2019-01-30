package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;

/**
 * User is an abstract class that implement Serializable all of our entities extends from user 
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public abstract class User implements Serializable {

	/** usrName is the user's username */
	private String usrName;
	/** password is the user's password */
	private String password;
	/** firstName is the user's firstName */
	private String firstName;
	/** lastName is the user's lastName */
	private String lastName;
	/** id is the user's id */
	private Integer id;
	/** email is the user's email */
	private String email;
	/** usrNuserTypeame is the user's userType */
	private UserType userType;
	
	/**
	 * User constructor
	 * @param usrName
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param id
	 * @param email
	 * @param userType
	 * @return User object
	 */
	public User(String usrName, String password, String firstName, String lastName, Integer id, String email, UserType userType) {
		this.usrName = usrName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.email = email;
		this.userType = userType;
	}
	/** toString override method from Object class */
	public String toString(){
		return usrName + password + firstName + lastName + id + email + userType;
	}
	/**
	 * getUsrName getter of user's username
	 * @return usrName
	 */
	public String getUsrName() {
		return usrName;
	}
	/**
	 * setUsrName setter of user's username
	 * @param usrName
	 */
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	/**
	 * getId getter of user's id
	 * @return id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * setId setter of user's id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * getPassword getter of user's password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * setPassword setter of user's password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * getFirstName getter of user's first name
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * setFirstName setter of user's first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * getLastName getter of user's lasr name
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * setLastName setter of user's last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * getEmail getter of user's email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * setEmail setter of user's email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * getUserType getter of user's type
	 * @return userType
	 */
	public UserType getUserType() {
		return userType;
	}
	/**
	 * setUserType setter of user's type
	 * @param userType
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
}
